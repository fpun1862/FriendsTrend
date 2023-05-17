package org.example;

import org.example.model.Item;
import org.example.model.Person;

import java.util.List;

// To help with this, we have the following 2 APIs. Complete the function that
// takes in a person and returns the items that the person's friends are buying.
public class FriendsService {

 /**
 * Returns a list of Items purchased by a Person. If purchased multiple times, an
 Item will be in the list multiple times.
 */
 public List<Item> getPurchases(Person person){
     return List.of();
 }

 /**
 * Returns a list of friends of a Person.
 */
 public List<Person> getFriends(Person person){
  return List.of();
 }
}
