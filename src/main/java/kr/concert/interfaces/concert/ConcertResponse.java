package kr.concert.interfaces.concert;

public class ConcertResponse {

    public record Rank(int rank, String concertName) {}

    public record CreateConcert(Long concertId, String concertName) {}
}
