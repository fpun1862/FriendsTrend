package org.example;

import org.example.model.Item;
import org.example.model.Person;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SuggestionServiceTest {

    private static final Person PERSON_1 = new Person("John", "John@email.com");
    private static final Person PERSON_2 = new Person("Pete", "Pete@email.com");
    private static final Person PERSON_3 = new Person("Max", "Max@email.com");
    private static final Person PERSON_4 = new Person("Gary", "Gary@email.com");
    private static final Item ITEM_1 = new Item("Toaster", "017a473a-d70e-45aa-ba8c-66bbc490f5af");
    private static final Item ITEM_2 = new Item("Kettle", "f2f76598-e0ec-4efc-89c5-01ec1817b455");
    private static final Item ITEM_3 = new Item("Microwave", "ab09ce68-b380-4bb3-b773-b9df641dec11");
    private static final Item ITEM_4 = new Item("Fridge", "24b88695-4029-4bea-a70b-3c5fc07b308b");
    private final FriendsService mockFriendsService = mock(FriendsService.class);
    private final SuggestionService suggestionService = new SuggestionService(mockFriendsService);

    @Test
    void shouldReturnEmptyListWhenNoFriends() {
        List<Item> suggestionsFromFriends = suggestionService.getSuggestionsFromFriends(PERSON_1);

        assertThat(suggestionsFromFriends).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenFriendsHaveNoItems() {
        when(mockFriendsService.getFriends(PERSON_1)).thenReturn(List.of(PERSON_2, PERSON_3));

        List<Item> suggestionsFromFriends = suggestionService.getSuggestionsFromFriends(PERSON_1);

        assertThat(suggestionsFromFriends).isEmpty();
    }

    @Test
    void shouldReturnSortedList() {
        when(mockFriendsService.getFriends(PERSON_1)).thenReturn(List.of(PERSON_2, PERSON_3, PERSON_4));
        when(mockFriendsService.getPurchases(PERSON_2)).thenReturn(List.of(ITEM_1, ITEM_2));
        when(mockFriendsService.getPurchases(PERSON_3)).thenReturn(List.of(ITEM_1, ITEM_2, ITEM_3));
        when(mockFriendsService.getPurchases(PERSON_4)).thenReturn(List.of(ITEM_1));

        List<Item> suggestionsFromFriends = suggestionService.getSuggestionsFromFriends(PERSON_1);

        assertThat(suggestionsFromFriends)
                .hasSize(3)
                .containsExactly(ITEM_1, ITEM_2, ITEM_3);
    }

    @Test
    void shouldReturnListFilteringOwned() {
        when(mockFriendsService.getFriends(PERSON_1)).thenReturn(List.of(PERSON_2, PERSON_3, PERSON_4));
        when(mockFriendsService.getPurchases(PERSON_1)).thenReturn(List.of(ITEM_2, ITEM_4));
        when(mockFriendsService.getPurchases(PERSON_2)).thenReturn(List.of(ITEM_1, ITEM_2));
        when(mockFriendsService.getPurchases(PERSON_3)).thenReturn(List.of(ITEM_1, ITEM_2, ITEM_3));
        when(mockFriendsService.getPurchases(PERSON_4)).thenReturn(List.of(ITEM_1));

        List<Item> suggestionsFromFriends = suggestionService.getSuggestionsFromFriends(PERSON_1);

        assertThat(suggestionsFromFriends)
                .hasSize(2)
                .containsExactly(ITEM_1, ITEM_3);
    }

    @Test
    void shouldReturnListFriendsCircle() {
        when(mockFriendsService.getFriends(PERSON_1)).thenReturn(List.of(PERSON_2, PERSON_3));
        when(mockFriendsService.getFriends(PERSON_3)).thenReturn(List.of(PERSON_2, PERSON_4));
        when(mockFriendsService.getPurchases(PERSON_1)).thenReturn(List.of(ITEM_2, ITEM_4));
        when(mockFriendsService.getPurchases(PERSON_2)).thenReturn(List.of(ITEM_1, ITEM_2));
        when(mockFriendsService.getPurchases(PERSON_3)).thenReturn(List.of(ITEM_1, ITEM_2, ITEM_3));
        when(mockFriendsService.getPurchases(PERSON_4)).thenReturn(List.of(ITEM_1));

        List<Item> suggestionsFromFriends = suggestionService.getSuggestionsFromCircle(PERSON_1);

        assertThat(suggestionsFromFriends)
                .hasSize(2)
                .containsExactly(ITEM_1, ITEM_3);
    }

}