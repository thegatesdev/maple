package com.github.thegatesdev.maple.io;

import com.github.thegatesdev.maple.element.Element;

import java.util.List;

public interface ReadResult {

    boolean succeeded();

    Element result();

    List<Failure> failures();


    record Failure(String path, String message) {
    }

    record FailedResult(List<Failure> failures) implements ReadResult {
        @Override
        public boolean succeeded() {
            return false;
        }

        @Override
        public Element result() {
            throw new RuntimeException("Cannot get result value for failed result");
        }
    }

    record SucceededResult(Element result, List<Failure> failures) implements ReadResult {

        @Override
        public boolean succeeded() {
            return true;
        }
    }
}
