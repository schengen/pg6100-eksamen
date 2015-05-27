package no.cengen.controller;

import no.cengen.TeamResult;
import no.cengen.entity.Result;
import no.cengen.infrastructure.EsportServiceManager;
import no.cengen.infrastructure.ResultDao;
import no.cengen.soap.service.Team;
import no.cengen.util.TeamResultUtil;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

@Model
public class ResultsController {
    @Inject
    private ResultDao resultDao;
    @Inject
    private EsportServiceManager manager;
    @Inject
    private TeamResultUtil teamResultUtil;

    public List<String> getGames() {
        return manager.getGames();
    }

    public List<TeamResult> getTeamResults(String game) {
        List<Team> teams = manager.getTeams(game);
        List<Result> results = resultDao.findAll();
        List<TeamResult> teamResults = teamResultUtil.aggregate(teams, results);
        Collections.sort(teamResults);

        return teamResults;
    }
}
