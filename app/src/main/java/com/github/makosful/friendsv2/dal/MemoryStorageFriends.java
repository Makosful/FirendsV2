package com.github.makosful.friendsv2.dal;

import com.github.makosful.friendsv2.be.Friend;

import java.util.ArrayList;
import java.util.List;

public class MemoryStorageFriends implements IStorage<Friend> {

    private static MemoryStorageFriends instance;

    public static MemoryStorageFriends getInstance() {
        if (instance == null)
            instance = new MemoryStorageFriends();

        return instance;
    }

    private List<Friend> friends;

    private MemoryStorageFriends()
    {
        friends = new ArrayList<>();
        // seed();
    }

    @Override
    public boolean create(Friend friend) {
        return friends.add(friend);
    }

    @Override
    public Friend readById(int id) {
        for (Friend f: friends)
        {
            if (f.getId() == id) return f;
        }

        return null;
    }

    @Override
    public List<Friend> readAll() {
        return new ArrayList<>(this.friends);
    }

    @Override
    public boolean update(Friend friend) {
        int index = friends.indexOf(readById(friend.getId()));

        if (index >= 0)
        {
            friends.set(index, friend);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAll() {
        friends.clear();
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        return friends.remove(readById(id));
    }

    @Override
    public void seed()
    {
        deleteAll();
        int i = 0;

        Friend friend = new Friend();
        friend.setId(++i);
        friend.setName("Bob Ross");
        friend.setPhone("51239875");
        friend.setEmail("bob@ross.paint");
        friend.setWebsite("https://www.bobross.com/");
        create(friend);

        friend = new Friend();
        friend.setId(++i);
        friend.setName("Tobi Danforth");
        friend.setAddress("14 Donald Trail");
        friend.setLatitude(35.23194);
        friend.setLongitude(40.32227);
        friend.setPhone("74494110");
        friend.setEmail("tdanforth0@dion.ne.jp");
        friend.setWebsite("http://opera.com/pellentesque/ultrices.html");
        create(friend);

        friend = new Friend();
        friend.setId(++i);
        friend.setName("Kelly Reijmers");
        friend.setAddress("838 4th Hill");
        friend.setLatitude(-0.5833333);
        friend.setLongitude(73.2333333);
        friend.setPhone("14812627");
        friend.setEmail("kreijmers1@mayoclinic.com");
        friend.setWebsite("https://census.gov/vestibulum/aliquet/ultrices/erat.aspx");
        create(friend);

        friend = new Friend();
        friend.setId(++i);
        friend.setName("Ruby Hakes");
        friend.setAddress("44027 Chive Hill");
        friend.setLatitude(13.3670968);
        friend.setLongitude(103.8448134);
        friend.setPhone("73742772");
        friend.setEmail("rhakes2@adobe.com");
        friend.setWebsite("https://yolasite.com/quisque.aspx");
        create(friend);

        friend = new Friend();
        friend.setId(++i);
        friend.setName("Marika Sasser");
        friend.setAddress("76 Walton Pass");
        friend.setLatitude(38.8466225);
        friend.setLongitude(139.8744722);
        friend.setPhone("45148295");
        friend.setEmail("msasser3@sphinn.com");
        friend.setWebsite("https://nature.com/nulla/neque.png");
        create(friend);

        friend = new Friend();
        friend.setId(++i);
        friend.setName("Cybil Peealess");
        friend.setAddress("0 Sunbrook Place");
        friend.setLatitude(40.0912332);
        friend.setLongitude(44.4037713);
        friend.setPhone("70365509");
        friend.setEmail("cpeealess4@google.co.jp");
        friend.setWebsite("https://dailymail.co.uk/in/sagittis/dui/vel/nisl/duis/ac.png");
        create(friend);

        friend = new Friend();
        friend.setId(++i);
        friend.setName("Carlye McSherry");
        friend.setAddress("516 Debra Place");
        friend.setLatitude(36.962751);
        friend.setLongitude(100.901228);
        friend.setPhone("69580095");
        friend.setEmail("cmcsherry5@paginegialle.it");
        friend.setWebsite("https://samsung.com/sapien/dignissim.json");
        create(friend);

        friend = new Friend();
        friend.setId(++i);
        friend.setName("Lib McKeating");
        friend.setAddress("5 Mockingbird Point");
        friend.setLatitude(9.939624);
        friend.setLongitude(126.065213);
        friend.setPhone("57057137");
        friend.setEmail("lmckeating6@amazon.de");
        friend.setWebsite("https://woothemes.com/erat/vestibulum/sed.jpg");
        create(friend);

        friend = new Friend();
        friend.setId(++i);
        friend.setName("Billy Berrisford");
        friend.setAddress("9 Fallview Plaza");
        friend.setLatitude(59.330173);
        friend.setLongitude(18.0551861);
        friend.setPhone("47435961");
        friend.setEmail("bberrisford7@cdc.gov");
        friend.setWebsite("http://europa.eu/eleifend/donec/ut/dolor/morbi/vel.xml");
        create(friend);

        friend = new Friend();
        friend.setId(++i);
        friend.setName("Adrian Artrick");
        friend.setAddress("9279 Main Trail");
        friend.setLatitude(9.338241);
        friend.setLongitude(-66.2575425);
        friend.setPhone("50258293");
        friend.setEmail("aartrick8@smh.com.au");
        friend.setWebsite("https://macromedia.com/ut/volutpat.jsp");
        create(friend);

        friend = new Friend();
        friend.setId(++i);
        friend.setName("Elinore Braidwood");
        friend.setAddress("19 Cody Lane");
        friend.setLatitude(-38.9898711);
        friend.setLongitude(175.8087485);
        friend.setPhone("77620275");
        friend.setEmail("ebraidwood9@washington.edu");
        friend.setWebsite("http://accuweather.com/cursus/vestibulum/proin.jsp");
        create(friend);
    }
}
