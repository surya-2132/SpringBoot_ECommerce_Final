package com.fullstackproject.ecommerce.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class TokenService {

    public static final String TOKEN_SECRET = "dfghsdDSF324dfsafsda";

    public String createTokenAndEncrypt(ObjectId userId) throws UnsupportedEncodingException {

        try{            //create algo then JWT -> withClaim -> sign
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            return JWT.create()
                    .withClaim("userId",userId.toString())
                    .withClaim("createAt", new Date())
                    .sign(algorithm);
        }catch (UnsupportedEncodingException | JWTCreationException e){
            e.printStackTrace();
        }
        return null;
    }

    public String verifyTokenAndDecrypt(String token){
        try{        //create Algo then verify with JWT -> decodedJwt -> getClaim
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier jwtVerifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            return decodedJWT.getClaim("userId").asString();
        } catch (UnsupportedEncodingException | JWTVerificationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
