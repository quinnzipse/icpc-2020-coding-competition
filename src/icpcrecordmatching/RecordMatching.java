package icpcrecordmatching;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RecordMatching {

    public static void main(String[] args) {
        var scanner = new Scanner(System.in);

        String line;
        ArrayList<Record> icpc_list = new ArrayList<>(), other_list = new ArrayList<>();
        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            if (line.length() > 0) {
                icpc_list.add(new Record(line));
            } else {
                break;
            }
        }

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            other_list.add(new Record(line));
        }

        solve(icpc_list, other_list);

    }

    private static class Record implements Comparable<Record> {
        public String first_name;
        public String last_name;
        public String email;
        public boolean matches;

        public Record(String line) {
            var scanner = new Scanner(line);

            this.first_name = scanner.next();
            this.last_name = scanner.next();
            this.email = scanner.next();
            this.matches = false;

            scanner.close();
        }

        @Override
        public int compareTo(Record o) {
            return this.email.toLowerCase().compareTo(o.email.toLowerCase());
        }
    }

    public static void solve(ArrayList<Record> icpc_list, ArrayList<Record> other_list) {
        for (Record current_record : icpc_list) {

            var stream = other_list.stream().filter(record -> record.email.equalsIgnoreCase(current_record.email) ||
                    (record.first_name.equalsIgnoreCase(current_record.first_name) && record.last_name.equalsIgnoreCase(current_record.last_name))).collect(Collectors.toList());

            if (stream.size() > 0) {
                current_record.matches = true;
                stream.forEach(record -> record.matches = true);
            }

        }

        boolean mismatches = !icpc_list.stream().allMatch(record -> record.matches) || !other_list.stream().allMatch(record -> record.matches);

        icpc_list.sort(Record::compareTo);
        other_list.sort(Record::compareTo);

        printLists(icpc_list, other_list, mismatches);

    }


    public static void printLists(ArrayList<Record> icpc_list, ArrayList<Record> other_list, boolean mismatches) {
        for (Record record : icpc_list) {
            if (!record.matches) {
                System.out.println("I " + record.email + " " + record.last_name + " " + record.first_name);
            }
        }
        for (Record record : other_list) {
            if (!record.matches) {
                System.out.println("O " + record.email + " " + record.last_name + " " + record.first_name);
            }
        }
        if (!mismatches) {
            System.out.println("No mismatches.");
        }
    }

}
