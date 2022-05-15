package ceng.estu.edu.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

// github.com/Tzesh
public class Node extends Thread {
    public HashSet<Node> prerequisites = new HashSet<>(); // prerequisite threads to checked if they've done to execute this one
    public String name; // name of this thread i.e., 'A'
    private Random random = new Random(); // random variable used to generate random numbers
    public Node(String name) {
        this.name = name;
    } // default constructor
    public void perform() { // perform method to start the thread
        this.start();
    }

    /**
     * simulation of running this node as a thread
     */
    @Override
    public void run() {
        try {
            waitForPrerequisites();
            System.out.println(String.format("Node%s is being started", name));
            Thread.sleep(random.nextInt(2000));
            System.out.println(String.format("Node%s is completed", name));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * waiting for prerequisite threads of this thread if necessary
     * @throws InterruptedException
     */
    private void waitForPrerequisites() throws InterruptedException {
        // upper part of this method is required to print whole string due to inconsistency of multithreading
        if (prerequisites.size() > 0) {
            String waitingFor = String.format("Node%s is waiting for ", name);
            int count = 0;
            for (Node node : prerequisites) {
                count++;
                waitingFor += node.name + (count == prerequisites.size() ? "" :  ",");
            }

            // if we don't print the whole string there is not such a guarantee that there will be none interrupt
            // (another output/print) between 2 print methods of ours
            System.out.println(waitingFor);

            // parsing all the prerequisite threads to array
            Node[] threads = prerequisites.toArray(new Node[prerequisites.size()]);

            // then waiting every prerequisite starting from first
            int index = 0;
            while (count > 0) {
                Node node = threads[index];
                if (node.isAlive()) {
                    // that means our thread is doing its work
                    Thread.sleep(random.nextInt(2000));
                } else {
                    // in this case we know that our thread is done
                    count--;
                    index++;
                    // keep going for another til the end
                }
            }
        }
    }

    /**
     * returns true if compared Node object has the same name
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Node && ((Node) obj).name.equals(this.name));
    }

    /**
     * hash mapping methodology uses 'hash codes' to check whether object is already presents or not
     * since we know that name of the threads are unique
     * our hashcode is unique according to the name of the object which is string
     * @return
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
