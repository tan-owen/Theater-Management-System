package utility;

import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Utility class for thread-safe file operations
 */
public class ThreadSafeFileManager {
    
    private static final ReadWriteLock FILE_LOCK = new ReentrantReadWriteLock();
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY = 100; // milliseconds

    /**
     * Acquires a read lock for file operations
     * @return the ReadWriteLock
     */
    public static ReadWriteLock getFileLock() {
        return FILE_LOCK;
    }

    /**
     * Performs a file operation with retry logic
     * @param operation The file operation to perform
     * @param operationName Name of the operation for logging
     * @return true if successful, false otherwise
     */
    public static boolean performFileOperationWithRetry(FileOperation operation, String operationName) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                operation.execute();
                return true;
            } catch (IOException e) {
                System.err.println(operationName + " - Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY * attempt); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return false;
                    }
                }
            }
        }
        System.err.println(operationName + " - Failed after " + MAX_RETRIES + " attempts");
        return false;
    }

    /**
     * Functional interface for file operations
     */
    @FunctionalInterface
    public interface FileOperation {
        void execute() throws IOException;
    }
}
