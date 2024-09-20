package me.jun.displayservice.support;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
abstract public class GuestbookFixture {

    public static final String GUESTBOOK_BASE_URL = "http://127.0.0.1";

    public static final int GUESTBOOK_PORT = 8082;
}
