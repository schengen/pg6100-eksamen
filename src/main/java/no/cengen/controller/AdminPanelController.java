package no.cengen.controller;

import no.cengen.entity.Result;
import no.cengen.entity.TeamResult;
import no.cengen.infrastructure.EsportDto;
import no.cengen.infrastructure.ResultDao;
import no.cengen.soap.service.Team;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Model
public class AdminPanelController {

    @Inject
    private ResultDao resultDao;
    @Inject
    private EsportDto esportDto;

    public List<String> getGames() { return esportDto.getGames(); }

    public List<TeamResult> getTeams(String game) {
        List<Team> teams = esportDto.getTeams(game);
        List<Result> results = getResults();
        List<TeamResult> teamResults = new ArrayList<>();
        for (Team t : teams) {
            List<Result> result = results.stream().filter(r -> r.getId()==t.getId()).collect(Collectors.toList());
            int wins = (int)result.stream().filter(r -> r.getWinner() == t.getId()).count();
            int losses = (int)result.stream().filter(r -> r.getLoser() == t.getId()).count();
            TeamResult teamResult = new TeamResult(t.getId(), wins, losses, t.getName());
            teamResults.add(teamResult);
        }

        return teamResults;
    }

    public List<Result> getResults() {
        return resultDao.findAll();
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/login.xhtml";
    }
}
