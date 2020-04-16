package de.jonashackt.springbootvuejs.controller;

import de.jonashackt.springbootvuejs.domain.User;
import de.jonashackt.springbootvuejs.exception.UserNotFoundException;
import de.jonashackt.springbootvuejs.repository.UserRepository;
import draft.query.Experiment;
import draft.query.formQuery;
import draft.utils.listStringToWeb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static de.jonashackt.springbootvuejs.controller.query.*;
import static draft.query.Experiment.*;
import static draft.query.processQuery2.getNameFile;
import static draft.warpscript.utils.*;

@CrossOrigin(origins = {"http://localhost:8080",
        "http://localhost:8081",
        "http://localhost:8082",
        "http://localhost:8083",
        "http://localhost:8084",
        "http://15.236.59.149:8080", // Amazon Web Service
        "http://15.236.59.149:8081",
        "http://15.236.59.149:8082",
        "http://15.236.59.149:8083",
        "http://15.236.59.149:8084",
        "http://193.55.95.161:8080",
        "http://localhost:3000",
        "http://localhost:4200"})
@RestController()
@RequestMapping("/api")
public class BackendController {

    private static final Logger LOG = LoggerFactory.getLogger(BackendController.class);

    public static final String HELLO_TEXT = "Hello from Spring Boot Backend!";
    public static final String QUERY_TEXT = "Hello from running a query from Spring Boot Backend!";
    public static final String SECURED_TEXT = "Hello from the secured resource!";

    static String token = "YevxHKGF6zmOOfTvxnP_76v1n_i3dAPm6DAyWCgKE5q5mfCc6ZHGuT5i40atfkit8YgqRBLBZ3EfPZIdLJMG5ck7sGQYUXy07gDelvmoM6BNKR_SaCrynV";
    static String commandToken = "'"+token+"' 'token' STORE";

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(path = "/runquery/Query 2 Area A T1 '2020-02-21T00:00:00.0Z' T2 '2020-02-28T00:00:00.0Z'")
    public String testQuery() throws InterruptedException, ParseException, IOException {
        String linkQuery = prepareQuery("Query 2 Area A T1 '2020-02-21T00:00:00.0Z' T2 '2020-02-28T00:00:00.0Z'");
        String kindQuery = getQuery(linkQuery);
        String kindArea = getArea(linkQuery);
        String From = getDate1(linkQuery);
        String To = getDate2(linkQuery);
        LOG.info("GET called on /hello/Query" + kindQuery+"/Area"+kindArea+"/From"+From+"/To"+To);
        formQuery FromQuery = new formQuery(kindQuery,kindArea,From,To);
        String Query = getNameFile(writeNameFile(FromQuery));
        String result = "";
        List<List<Double>> experiment = query.runQuery(kindQuery,kindArea,From,To);
        result = experiment.toString();
        return Query + "\n has the result: \n" + result + "++++++++++" + QUERY_TEXT ;
    }

    @RequestMapping(path = "/runquery/cachePrepare")
    public String runcachePrepare() {
        LOG.info("GET called on /cachePrepare resource");
        List<String> listCache = prepareCache();
        String temp = "";
        for (int i = 0; i < listCache.size(); i++){
            temp = temp + listCache.get(i) + "\n";
        }
        return temp;
    }

    @RequestMapping(path = "/runquery/queriesPrepare")
    public String runQueriesPrepare() {
        LOG.info("GET called on /QueriesPrepare resource");
        List<String> listCache = prepareQueries();
        String temp = "";
        for (int i = 0; i < listCache.size(); i++){
            temp = temp + listCache.get(i) + "\n";
        }
        return temp;
    }

    @RequestMapping(path = "/runquery/experiment")
    public String runExperiment() throws InterruptedException, ParseException, IOException {
        LOG.info("GET called on /experiment resource");
        String experiment = Experiment.main();
        return experiment + "++++++++++" + HELLO_TEXT;
    }
    @RequestMapping(path = "/runquery/clearMemcached")
    public String runClearMemcached() throws InterruptedException, ParseException, IOException {
        LOG.info("GET called on /clearMemcached resource");
        String experiment = clearMemcached();
        return experiment + "++++++++++" + HELLO_TEXT;
    }

    @RequestMapping(path = "/runquery/readAllMemcached")
    public String readMemcached() throws InterruptedException, ParseException, IOException {
        LOG.info("GET called on /readAllMemcached resource");
        List<String> allKey = readAllMemcached();
        String result = "";
        if (allKey.size() == 0)
            result = "There is no key on Memcached";
        else result = listStringToWeb.main(allKey);
        return result;
    }

    @RequestMapping(path = "/hello")
    public String sayHello() {
        LOG.info("GET called on /hello resource");
        return HELLO_TEXT;
    }
    @RequestMapping(path = "/testjson")//, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String sayJson() throws IOException, org.json.simple.parser.ParseException {
        LOG.info("GET called on /testjson resource");
        return testJson();
    }

    @RequestMapping(path = "/runquery/{query}/{area}/{from}/{to}")//, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String runQuery(@PathVariable("query") String Query,
                           @PathVariable("area") String Area,
                           @PathVariable("from") String From,
                           @PathVariable("to") String To) throws InterruptedException, ParseException, IOException {
        LOG.info("GET called on /hello/Query" + Query+"/Area"+Area+"/From"+From+"/To"+To);
        String fullQuery = "Query"+Query+"Area"+Area+"T1"+From+"T2"+To;
        String tempQuery = prepareQuery(fullQuery);
        String kindQuery = getQuery(tempQuery);
        String kindArea = getArea(tempQuery);
        String kindFrom = getDate1(tempQuery);
        String kindTo = getDate2(tempQuery);
        formQuery FromQuery = new formQuery(kindQuery,kindArea,kindFrom,kindTo);
        String standardQuery = getNameFile(writeNameFile(FromQuery));
        String result = "";
        List<List<Double>> experiment = query.runQuery(kindQuery,kindArea,kindFrom,kindTo);
        result = experiment.toString();
        return standardQuery + "\n has the result: \n" + result + "\n+++++++++++++++++++++++++++++++++\n" + QUERY_TEXT ;
    }

    @RequestMapping(path = "/runquery/compare/{query}/{area}/{from}/{to}")//, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String compareAQuery(@PathVariable("query") String Query,
                           @PathVariable("area") String Area,
                           @PathVariable("from") String From,
                           @PathVariable("to") String To) throws InterruptedException, ParseException, IOException, ExecutionException {
        LOG.info("GET called on runquery/compare/Query" + Query+"/Area"+Area+"/From"+From+"/To"+To);
        String fullQuery = "Query"+Query+"Area"+Area+"T1"+From+"T2"+To;
        String tempQuery = prepareQuery(fullQuery);
        String kindQuery = getQuery(tempQuery);
        String kindArea = getArea(tempQuery);
        String kindFrom = getDate1(tempQuery);
        String kindTo = getDate2(tempQuery);

        formQuery FromQuery = new formQuery(kindQuery,kindArea,kindFrom,kindTo);
        String standardQuery = getNameFile(writeNameFile(FromQuery));
        //List<List<Double>> experiment = query.runQuery(kindQuery,kindArea,kindFrom,kindTo);
        String result = "";
        result = compareSingleQuery(fullQuery);
        return standardQuery + "\n has the result: \n" + result + "\n+++++++++++++++++++++++++++++++++\n" + QUERY_TEXT ;
    }

    @RequestMapping(path = "/runquery/warpscript/{query}/{area}/{from}/{to}")//, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String warpscriptAQuery(@PathVariable("query") String Query,
                                @PathVariable("area") String Area,
                                @PathVariable("from") String From,
                                @PathVariable("to") String To) throws InterruptedException, ParseException, IOException, ExecutionException {
        LOG.info("GET called on runquery/warpscript/Query" + Query+"/Area"+Area+"/From"+From+"/To"+To);
        String fullQuery = "Query"+Query+"Area"+Area+"T1"+From+"T2"+To;
        String tempQuery = prepareQuery(fullQuery);
        String kindQuery = getQuery(tempQuery);
        String kindArea = getArea(tempQuery);
        String kindFrom = getDate1(tempQuery);
        String kindTo = getDate2(tempQuery);

        formQuery FromQuery = new formQuery(kindQuery,kindArea,kindFrom,kindTo);
        String standardQuery = getNameFile(writeNameFile(FromQuery));


        String warpScript = contentWarpScript(tempQuery, commandToken);
        //List<List<Double>> experiment = query.runQuery(kindQuery,kindArea,kindFrom,kindTo);
        //String result = "";
        //result = compareSingleQuery(fullQuery);
        return "The warp code is: \n" + warpScript + "\n+++++++++++++++++++++++++++++++++\n" + QUERY_TEXT ;
    }
    /*
    @RequestMapping(path = "/covid/{country}/{case}")//, method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public String covidEstimate(@PathVariable("country") String Country,
                                @PathVariable("case") String Case) throws InterruptedException, ParseException, IOException, ExecutionException {
        LOG.info("GET called on /covid/{country}/{case}" + "country: " +Country+"; Case: "+Case);
        String estimate = DREAM(Country, Case);
        return "The new case of " + Case +" is " +  estimate+"\n+++++++++++++++++++++++++++++++++\n" + QUERY_TEXT ;
    }
    */

    @RequestMapping(path = "/user/{lastName}/{firstName}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public long addNewUser (@PathVariable("lastName") String lastName, @PathVariable("firstName") String firstName) {
        User savedUser = userRepository.save(new User(firstName, lastName));

        LOG.info(savedUser.toString() + " successfully saved into DB");

        return savedUser.getId();
    }

    @GetMapping(path = "/user/{id}")
    public User getUserById(@PathVariable("id") long id) {

        return userRepository.findById(id).map(user -> {
            LOG.info("Reading user with id " + id + " from database.");
            return user;
        }).orElseThrow(() -> new UserNotFoundException("The user with the id " + id + " couldn't be found in the database."));
    }

    @RequestMapping(path="/secured", method = RequestMethod.GET)
    public @ResponseBody String getSecured() throws IOException, org.json.simple.parser.ParseException {
        LOG.info("GET successfully called on /secured resource");
        //String experiment = Experiment.main();
        return testJson();//SECURED_TEXT;// + "\n" + testJson();
    }

    // Forwards all routes to FrontEnd except: '/', '/index.html', '/api', '/api/**'
    // Required because of 'mode: history' usage in frontend routing, see README for further details
    @RequestMapping(value = "{_:^(?!index\\.html|api).*$}")
    public String redirectApi() {
        LOG.info("URL entered directly into the Browser, so we need to redirect...");
        return "forward:/";
    }


}
