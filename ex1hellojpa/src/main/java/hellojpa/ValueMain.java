package hellojpa;

public class ValueMain {

    public static void main(String args[]){
        Address address1 = new Address("city", "street", "zipcode");
        Address address2 = new Address("city", "street", "zipcode");

        //동일성 비교
        System.out.println("address1 == address2 : " + (address1 == address2)); // false

        //동등성 비교
        System.out.println("address1 equals address2 : " + address1.equals(address2));  // true
    }
}
