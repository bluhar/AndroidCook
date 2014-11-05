package com.example.photogallery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.net.Uri;
import android.util.Log;

public class FlickrFetcher {

    public static final String  TAG                 = "FlickrFetcher";
    public static final String  PREF_SEARCH_QUERY   = "searchQuery";
    public static final String  PREF_LAST_RESULT_ID = "lastResultId";

    private static final String ENDPOINT            = "https://api.flickr.com/services/rest";
    private static final String API_KEY             = "d7cdb8239d7b7da3d0134a33ac204631";
    private static final String METHOD_GET_RECENT   = "flickr.photos.getRecent";
    private static final String METHOD_SEARCH       = "flickr.photos.search";
    private static final String PARAM_EXTRAS        = "extras";
    private static final String EXTRA_SMALL_URL     = "url_s";
    private static final String PARAM_TEXT          = "text";

    private static final String XML_PHOTO           = "photo";

    //private static final String flickrKey = "d7cdb8239d7b7da3d0134a33ac204631"; //App is CookingCA, private key: 2c3170ed67e7339f

    public ArrayList<GalleryItem> downloadGalleryItems(String url) {
        ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();

        try {
            String xmlString = getUrl(url);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xmlString));
            parseItems(items, parser);

            Log.i(TAG, "Received xml:" + xmlString);
        }
        catch (IOException e) {
            Log.e(TAG, "Failed to fetch items", e);
        }
        catch (XmlPullParserException e) {
            Log.e(TAG, "Failed to parse items", e);
        }
        return items;
    }

    public ArrayList<GalleryItem> fetchItems() {
        String url = Uri.parse(ENDPOINT).buildUpon().appendQueryParameter("method", METHOD_GET_RECENT).appendQueryParameter("api_key", API_KEY).appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL).build().toString();
        return downloadGalleryItems(url);
    }

    public ArrayList<GalleryItem> search(String query) {
        String url = Uri.parse(ENDPOINT).buildUpon().appendQueryParameter("method", METHOD_SEARCH).appendQueryParameter("api_key", API_KEY).appendQueryParameter(PARAM_EXTRAS, EXTRA_SMALL_URL).appendQueryParameter(PARAM_TEXT, query).build().toString();
        return downloadGalleryItems(url);
    }

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        //由于flickr不能直接连接，需要VPN，可是收费的po vpn竟然也无法获取https://api.flickr.com/services/rest?method=flickr.photos.getRecent&api_key=d7cdb8239d7b7da3d0134a33ac204631&extras=url_s
        //但是用goagent可以访问，下面的代码偿试走本地的goagent来代理，可以无法成功....
        //        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8087));
        //        String proxyIp="localhost";          //代理Ip地址  
        //        int Port=8087;                         //代理提供的开放端口  
        //        /*构造Proxy对象，以适用于代理上网的方式*/  
        //        InetSocketAddress socketAddress=new InetSocketAddress(InetAddress.getByName(proxyIp),Port);  
        //        proxy=new Proxy(Proxy.Type.HTTP,socketAddress);  
        //        HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);

        URL url = new URL(urlSpec);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            InputStream inputStream = conn.getInputStream();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            return out.toByteArray();
        }
        finally {
            out.close();
            conn.disconnect();
        }
    }

    public String getUrl(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public void parseItems(ArrayList<GalleryItem> items, XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.next();
        /*
         * 
         * <photo id="15688336145" owner="60202180@N00" secret="bdd06d4290" 
         * server="3954" farm="4" 
         * title="Alexz Johnson in Burlington, VT" ispublic="1" 
         * isfriend="0" isfamily="0" 
         * url_s="https://farm4.staticflickr.com/3954/15688336145_bdd06d4290_m.jpg" 
         * height_s="160" width_s="240"/>
         * 
         */
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())) {
                String id = parser.getAttributeValue(null, "id");
                String caption = parser.getAttributeValue(null, "title");
                String smallUrl = parser.getAttributeValue(null, EXTRA_SMALL_URL);
                String owner = parser.getAttributeValue(null, "owner");

                GalleryItem item = new GalleryItem();
                item.setId(id);
                item.setCaption(caption);
                item.setUrl(smallUrl);
                item.setOwner(owner);
                
                items.add(item);
            }
            eventType = parser.next();
        }
    }

}
