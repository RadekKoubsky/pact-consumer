package org.rkoubsky.pact.example.springboot.consumer;

public class Consumer {

    public static void main(String[] args) {
        System.out.println(new Client("http://localhost:8080").getBook("978-1449373320"));
    }
}
