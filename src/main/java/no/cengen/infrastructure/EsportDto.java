package no.cengen.infrastructure;


import no.cengen.soap.service.*;

import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.List;

@Stateless
//TODO: feil bruk av dto
public class EsportDto {
    private EsportService_Service service;
    private EsportService port;

    public EsportDto() {
        service = new EsportService_Service();
        port = service.getEsportServicePort();
    }

    public List<String> getGames() {
        GameResponse response = null;
        try {
            response = port.getGames(AppConstants.CALLER_ID);
        } catch (SOAPException_Exception e) {
            e.printStackTrace();
        }
        return response.getGames().getGame();
    }

    public List<Team> getTeams(String game) {
        List<Team> teams = new ArrayList<>();
        try {
            TeamResponse response = port.getTeams(AppConstants.CALLER_ID, game);
            teams.addAll(response.getTeams().getTeam());
        } catch (SOAPException_Exception e) {
            e.printStackTrace();
        }
        return teams;
    }

    public List<Team> getAllTeams() {
        return aggregateTeams();
    }

    private List<Team> aggregateTeams() {
        List<Team> teams = new ArrayList<>();
        try {
            List<String> games = getGames();
            teams = collectTeamsFrom(games);
        } catch (SOAPException_Exception e) {
            e.printStackTrace();
        }
        return teams;
    }

    private List<Team> collectTeamsFrom(List<String> games) throws SOAPException_Exception {
        List<Team> teams = new ArrayList<>();
        for (String game : games) {
            teams.addAll(getTeams(game));
        }
        return teams;
    }

    //TODO: test throws exception with invalid CALLER_ID - BOTH CASES
    //TODO: test NO TEAMS with invalid GAME - getTeams CASE
}
