package ceng.estu.edu.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

// github.com/Tzesh
public class InputParser {
    /**
     * method used to parse lines from given path of input file
     * @param path path of the file
     * @return parsed lines
     * @throws IOException
     */
    public static List<String> parseLines(String path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(path))) {
            stream.forEach(line -> {
                lines.add(line);
            });
        }
        return lines;
    }
}
