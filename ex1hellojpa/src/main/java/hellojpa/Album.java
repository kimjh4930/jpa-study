package hellojpa;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity

//DTYPE에 넣을 구분자를 입력.
@DiscriminatorValue(value="A")
public class Album extends Item{
    private String artist;
}