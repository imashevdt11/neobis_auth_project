package kg.neobis.neobis_auth_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@Table(name = "confirmation_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    User user;

    @Column(name="token")
    String token;

    @Column(name="created_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date created_date;

    @Column(name="expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    Date expiry_date;

    public ConfirmationToken() {
        this.expiry_date = calculateExpiryDate();
    }

    public static Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, 5);
        return cal.getTime();
    }

    public boolean isExpired() {
        return new Date().after(this.expiry_date);
    }
}