package com.company;

import com.sun.source.doctree.ValueTree;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Node {
    private Node right;
    private Node left;
    private String value;
    private Person person;

    public Node(Node right, Node left, String value) {
        this.right = right;
        this.left = left;
        this.value = value;

    }

    public Node(Node right, Node left) {
        this.right = right;
        this.left = left;
    }

    public Node(String value, Person person) {
        this.right = null;
        this.left = null;
        this.value = value;
        this.person = person;
    }

    public Node() {
        this.right = null;
        this.left = null;
    }

    public void insert(String newValue, Person person){
        if (value == null){
            value = newValue;
            this.person = person;
        }
        else if (value.compareToIgnoreCase(newValue) > 0){
            if (left == null){
                left = new Node(newValue, person);
            }
            else {
                left.insert(newValue, person);
            }
        }
        else if (newValue.compareToIgnoreCase(value) > 0) {
            if (right == null) {
                right = new Node(newValue, person);
            } else {
                right.insert(newValue, person);
            }
        }
    }

    public void print(int x, int y, String[][] tree){
        if (value != null){
            tree[x][y] = value;
            if (right != null){
                right.print(x + 1, y + (int) Math.pow(2, getTreeHeight(0) - 2), tree);
            }
            if (left != null){
                left.print(x + 1, y - (int) Math.pow(2, getTreeHeight(0) - 2), tree);
            }
        }
    }

    public Person searchCPF(String value){
        if (this.value.equals(value)){
            return person;
        }
        else {
            if ((value.compareTo(this.value) > 0) && right != null){
                return right.searchCPF(value);
            }
            else if (left != null) {
                return left.searchCPF(value);
            }
            else {
                return null;
            }
        }
    }

    public void searchName(String value, ArrayList<Person> people){
        if (this.value.substring(0, value.length()).equalsIgnoreCase(value)){
            people.add(person);
        }
        if ((value.compareToIgnoreCase(this.value.substring(0, value.length())) > -1) && right != null){
            right.searchName(value, people);
        }
        if ((value.compareToIgnoreCase(this.value.substring(0, value.length())) < 1) && left != null) {
            left.searchName(value, people);
        }
    }

    public void searchDate(String start, String end, ArrayList<Person> people) {
        SimpleDateFormat formatter = new SimpleDateFormat("d/MM/yyyy");
        try {
            Date startDate = formatter.parse(start);
            Date endDate = formatter.parse(end);
            long startDateLong = TimeUnit.DAYS.convert(startDate.getTime(), TimeUnit.MILLISECONDS);
            long endDateLong = TimeUnit.DAYS.convert(endDate.getTime(), TimeUnit.MILLISECONDS);
            long valueLong = Long.parseLong(value);

            if (valueLong >= startDateLong && valueLong <= endDateLong){
                people.add(person);
            }
            if (valueLong <= endDateLong && right != null){
                right.searchDate(start, end, people);
            }
            if (valueLong >= startDateLong && left != null) {
                left.searchDate(start, end, people);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Integer getFactor(){
        Integer heightLeft = 0;
        Integer heightRight = 0;
        if (left != null){
            heightLeft = left.getTreeHeight(0);
        }
        if (right != null){
            heightRight = right.getTreeHeight(0);
        }
        return heightLeft - heightRight;
    }

    public Node getRight() {
        return right;
    }

    public Node getLeft() {
        return left;
    }

    public String getValue() {
        return value;
    }

    public Integer getTreeHeight(Integer height){
        Integer heightLeft = 0;
        Integer heightRight = 0;
        if (value == null){
            return height;
        }
        if (right != null){
            heightRight = right.getTreeHeight(height);
        }
        if (left != null){
            heightLeft = left.getTreeHeight(height);
        }
        if (heightLeft > heightRight){
            return heightLeft + 1;
        } else {
            return heightRight + 1;
        }
    }

    public Node clone(){
        Node newNode = new Node();
        newNode.setLeft(left);
        newNode.setRight(right);
        newNode.setValue(value);
        newNode.setPerson(person);
        return newNode;
    }

    public void rotateRight(){
        Node temp = this.clone();
        value = left.getValue();
        person = left.getPerson();
        left = left.getLeft();
        temp.left = temp.getLeft().getRight();
        right = temp;
    }

    public void rotateLeft(){
        Node temp = this.clone();
        value = right.getValue();
        person = right.getPerson();
        right = right.getRight();
        temp.right = temp.getRight().getLeft();
        left = temp;
    }

    public void doubleRotationRight(){
        left.rotateLeft();
        rotateRight();
    }

    public void doubleRotationLeft(){
        right.rotateRight();
        rotateLeft();
    }

    public String findLargest(String largestValue){
        String largestLeft = "";
        String largestRight = "";
        if (value != null) {
            if (left != null){
                largestLeft = left.findLargest("");
            }
            if (right != null){
                largestRight = right.findLargest("");
            }
            if (largestLeft.compareTo(largestRight) > 0){
                largestValue = largestLeft;
            } else {
                largestValue = largestRight;
            }
            if (value.compareTo(largestValue) > 0){
                largestValue = value;
            }
        }
        return largestValue;
    }

    public boolean checkBalance(){
        boolean balanceRight = true;
        boolean balanceLeft = true;
        if (right != null) {
            balanceRight = right.checkBalance();
        }
        if (left != null) {
            balanceLeft = left.checkBalance();
        }
        if (value != null){
            Integer factor = getFactor();
            if (factor > 1){
                if (left.getFactor() >= 0){
                    rotateRight();
                    balanceRight = false;
                } else if (left.getFactor() < 0){
                    doubleRotationRight();
                    balanceRight = false;
                }
            } else if (factor < -1){
                if (right.getFactor() <= 0){
                    rotateLeft();
                    balanceLeft = false;
                } else if (right.getFactor() > 0) {
                    doubleRotationLeft();
                    balanceLeft = false;
                }
            }
        }
        return balanceRight && balanceLeft;
    }

    public Node searchNode(String value){
        if (this.value.equals(value)){
            return this;
        }
        else {
            if ((value.compareTo(this.value) > 0) && right != null){
                return right.searchNode(value);
            }
            else if (left != null) {
                return left.searchNode(value);
            }
            else {
                return null;
            }
        }
    }

    public void remove(){
        if (left != null){
            String largestValue = left.findLargest("");
            Node largestLeft = searchNode(largestValue);
            value = largestLeft.getValue();
            largestLeft.remove();
        } else if (right != null) {
            value = right.getValue();
            right.remove();
        } else {
            Node toRemove = searchNode(value);
            toRemove = null;
            clear();
        }
        checkNull();
    }

    public void checkNull(){
        if (left != null){
            if (left.getValue() == null){
                left = null;
            }
            else {
                left.checkNull();
            }
        }
        if (right != null){
            if (right.getValue() == null){
                right = null;
            } else {
                right.checkNull();
            }
        }
    }

    public void clear(){
        left = null;
        right = null;
        value = null;
        person = null;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}

