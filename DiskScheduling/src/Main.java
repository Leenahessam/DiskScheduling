import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import static java.lang.Integer.parseInt;

public class Main {


    public static int[] sort(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++)
                if (arr[j] < arr[min])
                    min = j;

            int temp = arr[min];
            arr[min] = arr[i];
            arr[i] = temp;
        }
        return arr;
    }

    public static int FCFS(int[] requests) {
        Vector<Integer> served = new Vector<>();
        int total_head_movement = 0;
        System.out.println("Please, Enter the initial head start cylinder ");
        Scanner scan = new Scanner(System.in);
        int head = scan.nextInt();
        while (true) {
            if (!(head > -1) || !(head <= 200)) {
                System.out.println("Invalid input. The Initial head start cylinder should be between 0 and 200");
                System.out.println("Enter the initial head start cylinder");
                head = scan.nextInt();
            } else break;
        }
        //first come first served
        served.add(head);
        for (int request : requests)
            served.add(request);

        System.out.println("Sequence of head movements : ");
        for (int i = 0; i < served.size() - 1; i++)
            System.out.println("head moved from " + served.get(i) + " to " + served.get(i + 1));

        System.out.print("\nTotal head movements = ");

        for (int i = 0; i < served.size() - 1; i++) {
            if (served.get(i) < served.get(i + 1))
                System.out.print("(" + served.get(i + 1) + "-" + served.get(i) + ") ");
            else
                System.out.print("(" + served.get(i) + "-" + served.get(i + 1) + ") ");
            if (i != served.size() - 2)
                System.out.print("+ ");
            total_head_movement += Math.abs(served.get(i) - served.get(i + 1));
        }
        System.out.println("= " + total_head_movement);

        return total_head_movement;
    }


    public static int newOptimized(int[] request) {
        int total_head_movement = 0;
        Vector<Integer> served = new Vector<>();
        //Arrays.sort(requests);
        int[] requests;
        requests = sort(request);
        int head = 0;
        served.add(head);
        for (int i = head; i < requests.length; i++)
            served.add(requests[i]);

        System.out.println("\nSequence of head movements : ");
        for (int i = 0; i < served.size() - 1; i++)
            System.out.println("head moved from " + served.get(i) + " to " + served.get(i + 1));

        System.out.print("\nTotal head movements = ");

        for (int i = 0; i < served.size() - 1; i++) {
            System.out.print("(" + served.get(i + 1) + "-" + served.get(i) + ") ");
            if (i != served.size() - 2)
                System.out.print("+ ");
            total_head_movement += Math.abs(served.get(i) - served.get(i + 1));
        }
        System.out.println("= " + total_head_movement);
        return total_head_movement;

    }

    public static void main(String[] args) {
        boolean notHandled = false;
        File f = new File("input.txt");
        Scanner myReader = null;
        try {
            myReader = new Scanner(f);
        } catch (FileNotFoundException e) {
            System.out.println("File not found " + e);
        }
        String data = myReader.nextLine();
        myReader.close();
        System.out.println("data " + data);
        String[] req = data.split(",", data.length() - 1);
        int[] requests = new int[req.length];
        for (int i = 0; i < req.length; i++) {
            int req1 = parseInt(req[i]);
            if ((req1 > -1) && (req1 < 200))
                requests[i] = req1;
            else
                notHandled = true;
        }

        int THMFCFS = FCFS(requests);
        int THM_new_op = newOptimized(requests);
        //SSTF(requests);
        //scan_DiskScheduling(requests);


        if (THM_new_op < THMFCFS) {
            System.out.println("\nNewly Optimized algorithm is better.");
            System.out.println("As it requires minimun movements for the head in order to serve the disk I/O req.\n" +
                    "It has some advantages, for example: \n" +
                    "- Total head movements count is always less than or equal the position of the last disk's track.\n" +
                    "- It gives the best result, if all the input blocks are concentrated near the IDHP.\n" +
                    "- The algorithms is very simple, as total head movements count is always equal to the last input block's position");
        } else
            System.out.println("First Come First Served is better.");


        if (notHandled)
            System.out.println("Hint: There are some requests not handled as they're out of cylinder's range.");


    }

    public static int FindMinDis(int[] a, int num, boolean[] b) {
        int distance = 0;
        int min = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < a.length; i++) {
            distance = Math.abs(a[i] - num);
            if (distance < min && !b[i]) {
                min = distance;
                index = i;
            }
        }
        return index;
    }

    public static int SSTF(int[] requests) {
        Vector<Integer> served = new Vector<>();
        int total_head_movement = 0;
        System.out.println("SSTF Scheduling Algorithm.");
        System.out.println("Please, Enter the initial head start cylinder ");
        Scanner scan = new Scanner(System.in);
        int head = scan.nextInt();
        while (true) {
            if (!(head > -1) || !(head <= 200)) {
                System.out.println("Invalid input. The Initial head start cylinder should be between 0 and 200");
                System.out.println("Enter the initial head start cylinder");
                head = scan.nextInt();
            } else break;
        }
        //first come first served
        served.add(head);
        boolean[] accessed = new boolean[requests.length];
        for (int i = 0; i < accessed.length; i++)
            accessed[i] = false;

        int a = FindMinDis(requests, head, accessed);
        //served.add(head);
        accessed[a] = true;
        head = requests[a];
        served.add(head);
        for (int i = 0; i < requests.length; i++) {
            a = FindMinDis(requests, head, accessed);
            if (a == -1)
                break;
            head = requests[a];
            served.add(head);
            accessed[a] = true;
        }

        System.out.println("Sequence of head movements : ");
        for (int i = 0; i < served.size() - 1; i++)
            System.out.println("head moved from " + served.get(i) + " to " + served.get(i + 1));

        for (int i = 0; i < served.size() - 1; i++)
            total_head_movement += Math.abs(served.get(i) - served.get(i + 1));
        System.out.println("Total head movements " + total_head_movement);

        return total_head_movement;
    }

    public static int scan_DiskScheduling(int[] requests) {
        Vector<Integer> served = new Vector<>();
        int total_head_movement = 0;
        System.out.println("SCAN Scheduling Algorithm.");
        System.out.println("Please, Enter the initial head start cylinder ");
        Scanner scan = new Scanner(System.in);
        int head = scan.nextInt();
        while (true) {
            if (!(head > -1) || !(head <= 200)) {
                System.out.println("Invalid input. The Initial head start cylinder should be between 0 and 200");
                System.out.println("Enter the initial head start cylinder");
                head = scan.nextInt();
            } else break;
        }
        //first come first served
        served.add(head);
        boolean[] accessed = new boolean[requests.length];
        for (int i = 0; i < accessed.length; i++)
            accessed[i] = false;
        System.out.println("what's the direction \n1-left \n2-right");
        int dir = scan.nextInt();
        int node = head;
        if (dir == 1) {
            //loopleft
            for (int i = head; i > 0; i--) {
                for (int j = 0; j < requests.length; j++) {
                    if (requests[j] == i && !accessed[j]) {
                        served.add(requests[j]);
                        accessed[j] = true;
                    }
                }
            }
            served.add(0);
            for (int i = 0; i < 200; i++) {
                for (int j = 0; j < requests.length; j++) {
                    if (requests[j] == i && !accessed[j]) {
                        served.add(requests[j]);
                        accessed[j] = true;
                    }
                }
            }
            //
        }


        if (dir == 2) {
            //loopleft
            for (int i = head; i < 200; i++) {
                for (int j = 0; j < requests.length; j++) {
                    if (requests[j] == i && !accessed[j]) {
                        served.add(requests[j]);
                        accessed[j] = true;
                    }
                }
            }
            served.add(200);
            for (int i = 200; i < 0; i--) {
                for (int j = 0; j < requests.length; j++) {
                    if (requests[j] == i && !accessed[j]) {
                        served.add(requests[j]);
                        accessed[j] = true;
                    }
                }
            }
        }

        System.out.println("sequence of head movements : ");
        for (int i = 0; i < served.size() - 1; i++)
            System.out.println("from " + served.get(i) + " to " + served.get(i + 1));

        for (int i = 0; i < served.size() - 1; i++)
            total_head_movement += Math.abs(served.get(i) - served.get(i + 1));
        System.out.println("Total head movements " + total_head_movement);

        return total_head_movement;
    }

}
