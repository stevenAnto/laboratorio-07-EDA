import java.util.Vector;

public class BNodeGeneric<E extends Comparable<E>>{
  protected Vector<E> keys;
  protected Vector<BNodeGeneric<E>> children;
  int minDeg; //minimo grado
  int num;
  boolean isLeaf;


  //Constructor , parametro deg es el minimo, y en funcion de este se calcula el maximo
  public BNodeGeneric(int deg, boolean isLeaf){
    System.out.println("BNodeGeneric1");
    this.minDeg = deg;
    this.isLeaf =isLeaf;
    //Se generan Vector del tamano correspondiente
    this.keys = new Vector<E>(2*this.minDeg-1);
    this.children = new Vector<BNodeGeneric<E>>(2*this.minDeg);
    this.num = 0;
    for(int i=0;i< (2*this.minDeg);i++)
      this.keys.add(null);
    for(int i=0;i< (2*this.minDeg);i++)
      this.children.add(null);
    //System.out.println(keys);
    //System.out.println(children);

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
      boolean flag = i==num;
      if(children.get(i).num < minDeg)
	//Como abajo hay un proceso recursivo, se verifica la cantidad de claves del nodo por el cual se va  a bajar
	fill(i);
      if(flag && i > num)
	children.get(i-1).remove(key);
      else
	children.get(i).remove(key);
    }
  }
  public void removeFromLeaf(int i){
    //mover desde i
    for ( int k = i+1;k < num;++k){
      keys.set(k-1,keys.get(k));
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
  public void merge(int idx){
    BNodeGeneric<E> child = children.get(idx);
    BNodeGeneric<E> sibling = children.get(idx+1);
    //Inserto la ultima llave del actual nodo dentro de minDeg-1 position de l nodo hjijo
    child.keys.set(minDeg-1,this.keys.get(idx));
    //copio
    for(int i=0; i< sibling.num;++i)
      child.keys.set(i+minDeg, sibling.keys.get(i));
    if(!child.isLeaf){
      for(int i=0 ; i<= sibling.num; ++i)
	child.children.set(i+minDeg, sibling.children.get(i));
    }
    for(int i= idx+1; i<num;++i)
      this.keys.set(i-1,this.keys.get(i));
    for(int i= idx+2; i<=num; ++i)
      this.children.set(i-1,this.children.get(i));
    child.num += sibling.num+1;
    num--;
  }
  public E getSucesor(int i){
    BNodeGeneric<E>  current = children.get(i+1);
    while(!current.isLeaf)
      current = current.children.get(0);
    return current.keys.get(0);
  }
  //con este metodo lleno los hijos, prestandome de los hermanos o fusionando el padre
  public void fill(int i){
    if(i != 0 && children.get(i-1).num >= minDeg)
      borrowFromPrev(i);
    else if(i!= num && children.get(i+1).num >= minDeg)
      borrowFromNext(i);
    else{
      if(i!=num)
	merge(i);
      else
	merge(i-1);
    }
  }
  public void borrowFromPrev(int i){
    BNodeGeneric<E> child = children.get(i);
    BNodeGeneric<E> sibling = children.get(i+1);
    for(int j = child.num-1; j>=0;j--)
      child.keys.set(j+1,child.keys.get(j));
    if(!child.isLeaf){
      for(int k=child.num;k>=0;k--)
	child.children.set(k+1,child.children.get(k));
    }
    child.keys.set(0,this.keys.get(i-1));
    if(!child.isLeaf)
      child.children.set(0, sibling.children.get(sibling.num));
    this.keys.set(i-1,sibling.keys.get(sibling.num-1));
    child.num +=1;
    sibling.num -=1;
  }
  public void borrowFromNext(int i){
    BNodeGeneric<E> child = children.get(i);
    BNodeGeneric<E> sibling = children.get(i+1);
    child.keys.set(child.num,this.keys.get(i));
    if(!child.isLeaf)
      child.children.set(child.num+1,sibling.children.get(0));
    this.keys.set(i,sibling.keys.get(0));
    for(int j=1; j < sibling.num;++j)
      sibling.keys.set(j-1,sibling.keys.get(j));
    if(!sibling.isLeaf){
      for(int k=1; k<= sibling.num;++k)
	sibling.children.set(k-1,sibling.children.get(k));
    }
    child.num +=1;
    sibling.num -=1;
  }



  //Busco recursivamente en el nodo, con el while  me ubico en el indice por el cual bajar, o compararlo y con eso devuevl el nodo, caso contrario si es Leaf entonces no se encontro y se devuelve un null
  public BNodeGeneric<E> search(E key){
    int i=0;
    while(i< num && keys.get(i).compareTo(key)<0){
      System.out.println("entroBNodeWhie");
      i++;
    }
    System.out.println("salio:" +i);
    if(i<num && keys.get(i).compareTo(key)==0)
      return this;
    if(isLeaf)
      return null;
    return children.get(i).search(key);
  }
  public void insertNotFull(E key){
    int i= num-1;
    //Si ya estoy en hoja, procedo a insertar nomas
    //de adelante hacia atras voy desplazando
    if(isLeaf){
      while(i >=0 && keys.get(i).compareTo(key)>0){
	keys.set(i+1,keys.get(i));//desplazo
	i--;
      }
      //coloco la nueva clave
      keys.set(i+1,key);
      num++;
    }
    //si no es hoja
    else{
      //Encuentro el nodo en el cual sera insertado, a traves de de recorre el indice
      while(i>=0 && keys.get(i).compareTo(key)>0)
	i--;
      //Verificamos con anterioridad si se va a dividir dicho nodo
      if(children.get(i+1).num == 2*minDeg -1){
	splitChild(i+1,children.get(i+1));
	if(keys.get(i+1).compareTo(key)<0)
	  i++;
      }
      children.get(i+1).insertNotFull(key);
    }
  }
  public void splitChild(int i, BNodeGeneric<E> y){
    System.out.println("division");
    BNodeGeneric<E> z= new BNodeGeneric<E>(y.minDeg,y.isLeaf);
    z.num = minDeg-1;
    //pasamos las claves
    for(int j=0; j < minDeg-1;j++){
      z.keys.set(j,y.keys.get(j+minDeg));
    }
    //pasamos los hijos
    if(!y.isLeaf){
      for(int j = 0; j < minDeg; j++){
	z.children.set(j,y.children.get(j+minDeg));
      }
    }
    y.num =minDeg-1;
    //movemos los hijos, y referencias al nuevo nodo q se creo
    for(int j=num ; j>=i+1;j--)
      children.set(j+1,children.get(j));
    children.set(i+1,z);

    for(int j= num-1;j>=i;j--)
      keys.set(j+1,keys.get(j));
    keys.set(i,y.keys.get(minDeg-1));
    num++;
  }
  public void traverse(){
    int i;
    for(i=0;i<num;i++){
      if(!isLeaf){
	children.get(i).traverse();
      }
      System.out.print(keys.get(i));
    }
    if(!isLeaf){
      children.get(i).traverse();
    }
  }
}
