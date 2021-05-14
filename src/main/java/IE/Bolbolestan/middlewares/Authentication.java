package IE.Bolbolestan.middlewares;

import IE.Bolbolestan.bolbolestanExceptions.EmailOrPasswordIncorrectException;
import IE.Bolbolestan.student.StudentEntity;
import IE.Bolbolestan.student.StudentRepository;
import IE.Bolbolestan.tools.Password;

import java.sql.SQLException;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


public class Authentication {

    public static String authenticate(String email, String password) throws SQLException, EmailOrPasswordIncorrectException {
        StudentEntity studentEntity = StudentRepository.getInstance().findStudentByEmail(email);
        if (studentEntity == null) {
            throw new EmailOrPasswordIncorrectException();
        }

        if (!studentEntity.getPassword().equals(Password.hash(password))) {
            throw new EmailOrPasswordIncorrectException();
        }

        return Authentication.createJWT(studentEntity.getStudentId());
    }

    public static StudentEntity getAuthenticated(String studentId) {
        try {
            return StudentRepository.getInstance().getById(studentId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public static Claims decodeJWT(String token) {

        String base64SecretKey = Base64.getEncoder().
                encodeToString(Config.SECRET_KEY.getBytes());

        return Jwts.parser()
                .setSigningKey(DatatypeConverter.
                        parseBase64Binary(base64SecretKey))
                .parseClaimsJws(token).getBody();
    }

    public static String createJWT(String studentId) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        String base64SecretKey = Base64.getEncoder().
                encodeToString(Config.SECRET_KEY.getBytes());

        byte[] apiKeySecretBytes = DatatypeConverter.
                parseBase64Binary(base64SecretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes,
                signatureAlgorithm.getJcaName());

        long expMillis = nowMillis + Config.EXP_TIME;
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder()
                .setId(studentId)
                .setIssuedAt(now)
                .setIssuer(Config.TOKEN_ISSUER)
                .setExpiration(exp)
                .signWith(signatureAlgorithm,signingKey);

        return builder.compact();
    }

}
