//package com.example.project;
public class BTreeGeneric<E extends Comparable<E>> {
  BNodeGeneric<E> root;
  int minDeg;

  public BTreeGeneric(int deg){
    this.root = null;
    this.minDeg = deg;
  }
  

    public boolean add(E value) {
        //TODO implement here!
        return false;
    }

    public E remove(E value) {
        //TODO implement here!
        return null;
    }

    public void clear() {
        //TODO implement here!
    }

    public boolean search(E value) {
      return searchR(value)!=null;
    }
    private BNodeGeneric<E> searchR(E value) {
      return root.search(value);
    }

    public int size() {
        //TODO implement here!
        return 0;
    }
}
