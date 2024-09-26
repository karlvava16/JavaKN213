package itstep.learning.services;

public class Md5HashService implements HashService {

    @Override
    public String hash(String string) {
        return "Md5HashService";
    }
}
