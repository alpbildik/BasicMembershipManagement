package basicmembermanagement.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "MEMBER")
@Getter
@Setter
public class Member implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "PARCEL")
    private String parcel;

    @Column(name = "STATUS")
    private Long status = 1L;

    @Column(name= "PHONE")
    private String phone;

    public String getInfo(){
        return name + " " + surname + " " + parcel;
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj)) return false;
        if (!(obj instanceof Member)) return false;
        Member othMember = (Member) obj;
        return Objects.nonNull(this.id) && Objects.nonNull(othMember.getId()) && this.id.equals(othMember.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
