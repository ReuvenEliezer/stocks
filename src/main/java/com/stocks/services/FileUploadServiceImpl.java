package com.stocks.services;

import com.stocks.entities.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    /**
     * https://www.vinsguru.com/rsocket-file-upload-example/
     */

    @Value("${output.file.path:src/test/resources/output}")
    private Path outputPath;

    @Override
    public Flux<Status> uploadFile(Path path, Flux<DataBuffer> bufferFlux) throws IOException {
        Path opPath = outputPath.resolve(path);
        AsynchronousFileChannel channel = AsynchronousFileChannel
                .open(opPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        return DataBufferUtils.write(
                bufferFlux
//                        bufferFlux.delayElements(Duration.ofSeconds(1))
                        , channel)
                .map(b -> Status.CHUNK_COMPLETED);
//                .doOnNext(s -> logger.info("Upload Status : {}", Status.CHUNK_COMPLETED))
//                .doOnComplete(() -> logger.info("done to Upload file to '{}'", path));
    }



}