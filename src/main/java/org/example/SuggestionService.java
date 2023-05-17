package org.example;

import org.example.model.Item;
import org.example.model.Person;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SuggestionService {

    private final FriendsService friendsService;

    public SuggestionService(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    // An online retailer is releasing a new feature called "What are my friends
    // buying?". The feature lists a set of products that you have not already
    // purchased from the things that your friends have bought, in order of most bought
    // to least bought.
    public List<Item> getSuggestionsFromFriends(Person person) {
        List<Person> friends = friendsService.getFriends(person);
        List<Item> items = friendsService.getPurchases(person);

        return getItems(friends, items);
    }

    private List<Item> getItems(List<Person> friends, List<Item> items) {
        Map<Item, Long> friendsItemsMap =  friends.stream().map(friendsService::getPurchases)
                .flatMap(List::stream)
                .filter( item -> !items.contains(item))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return friendsItemsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();
    }

    //    extension 1
    //    - Instead of your friends, what about your social circle? A social circle of a person is defined as the
    //    person's friends and their friends.
    public List<Item> getSuggestionsFromCircle(Person person) {
        List<Person> friends = friendsService.getFriends(person);
        List<Item> items = friendsService.getPurchases(person);

        Stream<Person> friendsStream = friends.stream().map(friendsService::getFriends)
                .flatMap(List::stream);
        List<Person> friendsCircle = Stream.concat(friendsStream, friends.stream()).distinct().toList();

        return  getItems(friendsCircle, items);
    }

    /*


extension 2
- What about extend to N level friends circle?

*/

}
