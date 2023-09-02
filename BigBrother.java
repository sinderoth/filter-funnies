import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class BigBrother {
    private final List<Message> messages = new ArrayList<>();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private List<String> bannedWords;


    public BigBrother() {
        try (Stream<Path> paths = Files.walk(Paths.get("dialogs"))) {
            paths
                    .filter(Files::isRegularFile)
                    .forEach(dialogFile -> {
                        try (Stream<String> lines = Files.lines(Paths.get(dialogFile.toString()))) {
                            this.messages.addAll(lines
                                    .map(row -> new Message(row.split("\t")))
                                    .toList());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Stream<String> lines = Files.lines(Paths.get("bannedWordList.txt"))) {
            this.bannedWords = lines
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        BigBrother bb = new BigBrother();

        // You can play around
        // bb.printSuspiciousMessages();
        // System.out.println(bb.getSenderMessageCount("gear4", "2012-04-09"));
        // System.out.println(bb.getUniqueReceivers("2012-04-09"));
        // System.out.println(bb.getSuspiciousMessageCounts());
        System.out.println(bb.getTopReceiversBySender("sacarlson", 10));
        // System.out.println(bb.getSentReceivedRatio());

    }

    /**
     * Prints all messages that contain any of the banned words.
     */
    public void printSuspiciousMessages() {
        List<Message> output = messages.stream().filter(this::containsBannedWord).toList();
        output.stream().distinct().forEach(System.out::println);
    }

    public boolean containsBannedWord(Message m) {
        return bannedWords.stream()
                .anyMatch(bannedWord -> m.getMessage().toLowerCase().contains(bannedWord));
    }

    /**
     * counts the messages that is sent by sender on a given date
     *
     * @param sender  name of the sender
     * @param dateStr date
     * @return count message count
     */
    public long getSenderMessageCount(String sender, String dateStr) {
        long count = -1;
        LocalDate date = LocalDate.parse(dateStr, dateTimeFormatter);
        count = messages.stream().filter(mes -> mes.getSender().equals(sender) && mes.getDate().equals(date)).count();
        return count;
    }

    /**
     * find all unique receivers that received message on a given date.
     *
     * @param dateStr date
     * @return collectedMessages list of unique receiver
     */
    public List<String> getUniqueReceivers(String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, dateTimeFormatter);

        return messages.stream()
                .filter(message -> message.getDate().equals(date))
                .map(Message::getReceiver)
                .filter(receiver -> !receiver.isEmpty())
                .distinct()
                .collect(toList());
    }

    /**
     * get the count of suspicious messages sent for every one of those
     *
     * @return suspiciousMessageCounts sender and count
     */
    public Map<String, Long> getSuspiciousMessageCounts() {
        return messages.stream()
                .filter(this::containsBannedWord)
                .collect(Collectors.groupingBy(Message::getSender, Collectors.counting()));
    }

    /**
     * get the most N receiver by a sender
     *
     * @param sender name of the sender
     * @param topN   at most
     * @return topNReceivers receivers and message count.
     */

    public Map<String, Long> getTopReceiversBySender(String sender, int topN) {
        Map<String, Long> countsBySender = messages.stream()
                .filter(message -> message.getSender().equals(sender) && !message.getReceiver().isEmpty())
                .collect(Collectors.groupingBy(Message::getReceiver, Collectors.counting()));

        List<Map.Entry<String, Long>> sortedReceivers = countsBySender.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .toList();

        Map<String, Long> topNReceivers = new LinkedHashMap<>();
        sortedReceivers.forEach(entry -> topNReceivers.put(entry.getKey(), entry.getValue()));
        return topNReceivers;
    }

    /**
     * get the ratio of sent and received messages for everyone
     *
     * @return ratio name of the person and their ratio
     */
    public Map<String, Double> getSentReceivedRatio() {
        Map<String, Long> received = messages.stream().filter(m -> !m.getReceiver().isEmpty())
                .collect(Collectors.groupingBy(Message::getReceiver, Collectors.counting()));

        Map<String, Long> sent = messages.stream()
                .collect(Collectors.groupingBy(Message::getSender, Collectors.counting()));

        return received.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, t -> (double) sent.get(t.getKey()) / received.get(t.getKey())));
    }
}
