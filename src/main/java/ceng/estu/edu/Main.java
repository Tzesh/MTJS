package ceng.estu.edu;

import ceng.estu.edu.utils.Node;
import ceng.estu.edu.utils.InputParser;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.out;

// github.com/Tzesh
public class Main {
    @Option(name = "-i", aliases = "--i", usage = "Path of the input file to be processed", required = true)
    private String path; // path argument
    Set<Node> threads = new HashSet<>(); // set variable to store and use threads
    List<String> lines = new ArrayList<>(); // arraylist variable to store and use lines

    /**
     * main method of the project
     * @param args
     */
    public static void main(String[] args) {
        final Main instance = new Main(); // creating an instance of main class
        try {
            instance.getArgs(args); // getting and controlling the argument
            instance.getLines(); // reading lines from given path
            instance.createThreads(); // creating threads according to given execution order
            instance.executeThreads(); // executing all the threads in set
        } catch (IOException ex) {
            out.println("An unexpected I/O Exception has been occurred: " + ex);
        }
    }

    /**
     * getting the arguments and storing them into the arraylist that we've created
     * @param args
     * @throws IOException
     */
    private void getArgs(final String[] args) throws IOException {
        final CmdLineParser parser = new CmdLineParser(this);
        if (args.length < 1) {
            parser.printUsage(out);
            System.exit(-1);
        }
        try {
            parser.parseArgument(args);
            if (!Files.exists(Path.of(path))) {
                out.println("Given path is not correct!");
                parser.printUsage(out);
                System.exit(-1);
            }
        } catch (CmdLineException ex) {
            out.println("Unable to parse command-line options: " + ex);
        }
    }

    /**
     * getting all the lines in the given input
     */
    private void getLines() {
        try {
            lines = InputParser.parseLines(path);
        } catch (IOException e) {
            out.println("Unexpected error has been occurred during read operation of the desired input file.");
        }
    }

    /**
     * creating threads according to the lines
     */
    private void createThreads() {
        for (int i = 0; i < lines.size(); i++) {
            String[] flow = lines.get(i).split("->");

            if (flow.length == 1) {
                Node thread = new Node(flow[0]);
                thread = findOrGet(thread);
                if (!threads.contains(thread))
                    threads.add(thread);
                continue;
            }

            String[] prerequisites = flow[0].split(",");
            HashSet<Node> prerequisiteThreads = new HashSet<>();
            for (String prerequisite : prerequisites) {
                Node thread = new Node(prerequisite);
                thread = findOrGet(thread);
                prerequisiteThreads.add(thread);
                if (!threads.contains(thread))
                    threads.add(thread);
            }

            String partialThread = flow[1];
            Node thread = new Node(partialThread);
            thread = findOrGet(thread);
            thread.prerequisites = prerequisiteThreads;
            if (!threads.contains(thread)) threads.add(thread);
        }
    }

    /**
     * method to check whether given thread is exists or not if it is present then return that thread
     * @param thread thread to be checked
     * @return
     */
    private Node findOrGet(Node thread) {
        final String name = thread.name;
        return threads.contains(thread) ? threads.stream().filter(thread1 -> thread1.name.equals(name)).findFirst().get() : thread;
    }

    /**
     * executing threads using their perform method
     */
    private void executeThreads() {
        for (Node node : threads) {
            node.perform();
        }
    }
}
