import java.util.Vector;

public class BNodeGeneric<E extends Comparable<E>>{
  protected Vector<E> keys;
  protected Vector<BNodeGeneric<E>> children;
  int minDeg; //minimo grado
  int num;
  boolean isLeaf;


  //Constructor , parametro deg es el minimo, y en funcion de este se calcula el maximo
  public BNodeGeneric(int deg, boolean isLeaf){
    this.minDeg = deg;
    this.isLeaf =isLeaf;
    //Se generan Vector del tamano correspondiente
    this.keys = new Vector<E>(2*this.minDeg-1);
    this.children = new Vector<BNodeGeneric<E>>(2*this.minDeg);
    this.num = 0;

  }
  //Devuelve el indice de la llave buscada o el siguiente mayor
  public int findKey(E key){
    int i=0;
    while (i<num && keys.get(i).compareTo(key)<0){
      i++;
    }
    return i;
  }
  public void remove(E key){
    int i= findKey(key);
    if(i < num && keys.get(i).compareTo(key)==0){
      if(isLeaf) removeFromLeaf(i);
      else removeFromNonLeaf(i);
    }
    else{
      //Como es hoja, entonces se no ha encontrado dicha clave
      if(isLeaf){
	System.out.println("no existe dicha clave");
	return;
      }
      //el booleano flag nos indica que ha comparado todo en el nodo, pero va a seguir buscando en el subarbol
      boolean flag = i ==num;
      if(children.get(i).num < minDeg)
	//no se sabe que hace fill
	fill(i);
      if(flag && i > num)
	children.get(i-1).remove(key);
      else
	children.get(i).remove(key);
    }
  }
  public void removeFromLeaf(int i){
    //mover desde i
    for ( int k = i+1;k < num;k++){
      keys.set(i-1,keys.get(i));
    }
    num--;
  }
  public void removeFromNonLeaf(int i){
    E key =  keys.get(i);
    //Como no es hoja, entonces 
    //if children.get(i) tiene menos keys que minDeg, entoncee check el hermano childre.get(i+1)
    //si el hermano tiene al menos el minDef, entonces entonces el succesor o antecesor recurvivamente in children.get(i+1)
    if(children.get(i+1).num >= minDeg){
      //?????
      E sucesor = getSucesor(i);
      keys.set(i,sucesor);
      children.get(i+1).remove(sucesor);
    }
    else{
      merge (i);//como lo hermanos no tiene para prestarse, entonces se fusionan con merge
      children.get(i).remove(key);
    }
  }
  public E getSucesor(int i){
    BNodeGeneric<E>  current = children.get(i+1);
    while(!current.isLeaf)
      current = current.children.get(0);
    return current.keys.get(0);
  }
  public void fill(int i){
  }
  public BNodeGeneric<E> search(E key){
    int i=0;
    while(i< num && key.compareTo(keys.get(i))>0){
      i++;
    }
    if(keys.get(i).compareTo(key)==0)
      return this;
    if(isLeaf)
      return null;
    return children.get(i).search(key);
  }
}
