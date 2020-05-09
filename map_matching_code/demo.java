package com.graphhopper.matching;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.graphhopper.GraphHopper;
import com.graphhopper.matching.gpx.Gpx;
import com.graphhopper.reader.osm.GraphHopperOSM;
import com.graphhopper.routing.querygraph.VirtualEdgeIteratorState;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.HintsMap;
import com.graphhopper.storage.index.QueryResult;
import com.graphhopper.util.EdgeIteratorState;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class demo {
    private static XmlMapper xmlMapper = new XmlMapper();

    public static void testSingleFile(MyGraphHopper hopper, MapMatching mapMatching, String fileName) throws IOException {
        // Need to reduce GPS accuracy because too many GPX are filtered out otherwise.
        mapMatching.setMeasurementErrorSigma(40);
        InputStream inputStream = new FileInputStream("D:\\repos\\third-party\\map-matching\\matching-web\\src\\test\\resources\\grab_sg\\" + fileName);
        Gpx gpx = xmlMapper.readValue(inputStream, Gpx.class);
        inputStream.close();
        MatchResult mr = mapMatching.doWork(gpx.trk.get(0).getEntries());

        List<EdgeMatch> edgeMatches = mr.getEdgeMatches();

//        for (EdgeMatch edgeMatch : edgeMatches) {
//            EdgeIteratorState edgeState = edgeMatch.getEdgeState();
//            int edgeId = edgeState.getEdge();
//            String vInfo = "";
//            if (edgeState instanceof VirtualEdgeIteratorState) {
//                // first, via and last edges can be virtual
//                VirtualEdgeIteratorState vEdge = (VirtualEdgeIteratorState) edgeState;
//                edgeId = vEdge.getOriginalEdgeKey() / 2;
//                vInfo = "v";
//            }
//            System.out.println(edgeState.getName());
//            System.out.println("(" + vInfo + edgeId + ")");
//            System.out.println("A");
//        }
    }

    public static void testSnapEdgeStandard() throws IOException {
        CarFlagEncoder encoder = new CarFlagEncoder();
        MyGraphHopper hopper = new MyGraphHopper();
        hopper.setDataReaderFile("map-data/malaysia-singapore-brunei-latest.osm.pbf");
        hopper.setGraphHopperLocation("target/mapmatchingtest-grab");
        hopper.setEncodingManager(EncodingManager.create(encoder));
        hopper.getCHFactoryDecorator().setDisablingAllowed(true);
        hopper.importOrLoad();

        HintsMap opts = new HintsMap();

        MapMatching mapMatching = new MapMatching(hopper, opts);
        long startTime = System.nanoTime();

        for(Integer i = 2; i <= 200; i += 1) {
            try {
                System.out.println(i);
                testSingleFile(hopper, mapMatching, i + ".gpx");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000000;
        System.out.println("TOTAL TIME " + duration);
        Integer b = 1;
    }

    public static void main(String[] args) {
        try {
            testSnapEdgeStandard();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
