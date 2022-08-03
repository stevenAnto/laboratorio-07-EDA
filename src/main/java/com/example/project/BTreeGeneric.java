//package com.example.project;
public class BTreeGeneric<E extends Comparable<E>> {
  BNodeGeneric<E> root;
  int minDeg;

  public BTreeGeneric(int deg){
    this.root = null;
    this.minDeg = deg;
  }
  

    public boolean add(E value) {
        return false;
    }
    public void insert(E key){
      System.out.println("se insertara "+key);
      if(root == null){
	System.out.println("inset1.1");
	root = new BNodeGeneric<E>(minDeg,true);//true porque es una hoja
	root.keys.set(0,key);
	root.num = 1;
      }
      else{
	System.out.println("insert1.2");
	//la raiz esta llena el arbol crecera
	if(root.num==2*minDeg-1){
	  BNodeGeneric<E> s = new BNodeGeneric<E>(minDeg,false);
	  s.children.set(0,root);
	  s.splitChild(0,root);
	  int i=0;
	  if(s.keys.get(0).compareTo(key)<0)
	    i++;
	  s.children.get(i).insertNotFull(key);
	  root = s;

	}
	else
	  root.insertNotFull(key);
      }
      System.out.println("se termino de insertar "+key+"\n");
    }
    public void traverse(){
      System.out.println("se imprime arbol");
      if(root !=null){
	root.traverse();
      }
      System.out.println();
    }

    public void remove(E value) {
      System.out.println("Se eliminara a :"+value);
      if(root == null){
	System.out.println("The tree is empty");
	return;
      }
      root.remove(value);
      //si la raiz se queda sin claves se procede a nombra a su hijo como nueva raizo
      if(root.num==0){
	if(root.isLeaf)
	  root = null;
	else
	  root = root.children.get(0);
      }

    }

    public void clear() {
        //TODO implement here!
    }

    /*public boolean search(E value) {
      return searchR(value)!=null;
    }
    private BNodeGeneric<E> searchR(E value) {
      return root.search(value);
    }*/
    public BNodeGeneric<E> search(E key) {
      return root == null ? null : root.search(key);
    }

    public static void main(String[] args){
      BTreeGeneric<Integer> t = new BTreeGeneric<Integer>(2);
      t.insert(1);
      t.traverse();
      t.insert(2);
      t.insert(3);
      t.traverse();
      t.insert(4);
      t.insert(5);
      t.traverse();
      t.insert(6);
      t.insert(7);
      t.insert(8);
      t.insert(9);
      t.insert(10);
      t.insert(11);
      t.insert(12);
      t.insert(13);
      t.insert(14);
      t.insert(15);
      t.insert(16);
      t.insert(17);
      t.insert(18);
      t.insert(19);
      t.traverse();
      t.remove(17);
      t.traverse();
      System.out.println("se busca a 18 :"+t.search(18));
      t.search(50);
      System.out.println("se busca a 50 :"+t.search(50));
      t.remove(18);
      t.traverse();
    }
}
