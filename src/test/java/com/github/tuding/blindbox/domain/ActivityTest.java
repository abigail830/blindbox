package com.github.tuding.blindbox.domain;

import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ActivityTest {

    @Test
    void getNotifier_1() {
        final Activity activity = new Activity();
        activity.setNotify("oTA-N5rCXJmZsaDKxnB4vA1Tle8I");
        final Set<String> notifierAsSet = activity.getNotifierAsSet();
        System.out.println(notifierAsSet);
        assertEquals(1, notifierAsSet.size());
    }

    @Test
    void getNotifier_2() {
        final Activity activity = new Activity();
        activity.setNotify("oTA-N5rCXJmZsaDKxnB4vA1Tle8I|oTA-N5rCXJmZsaDKxnB4vA1Tle8J");
        final Set<String> notifierAsSet = activity.getNotifierAsSet();
        System.out.println(notifierAsSet);
        assertEquals(2, notifierAsSet.size());
    }

    @Test
    void add_notify() {
        final Activity activity = new Activity();
        activity.setActivityStartDate(Timestamp.valueOf(LocalDateTime.now().plus(1, DAYS)));
        activity.setNotify("oTA-N5rCXJmZsaDKxnB4vA1Tle8I");
        activity.addNotifyInfo("oTA-N5rCXJmZsaDKxnB4vA1Tle8J", "page");
        System.out.println(activity);
        assertEquals(2, activity.getNotifierAsSet().size());
        assertEquals("page", activity.getNotifyJumpPage());
        assertEquals("oTA-N5rCXJmZsaDKxnB4vA1Tle8I|oTA-N5rCXJmZsaDKxnB4vA1Tle8J", activity.getNotify());
    }
}