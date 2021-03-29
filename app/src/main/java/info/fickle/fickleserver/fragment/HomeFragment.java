package info.fickle.fickleserver.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import info.fickle.fickleserver.adapter.MainAdapter;
import info.fickle.fickleserver.R;
import info.fickle.fickleserver.model.MainModel;
import info.fickle.fickleserver.wifihotspotutils.ClientScanResult;
import info.fickle.fickleserver.wifihotspotutils.FinishScanListener;
import info.fickle.fickleserver.wifihotspotutils.WifiApManager;


public class HomeFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPageNo;
    RecyclerView rv;
    private AlertDialog.Builder alertDialog;
    FloatingActionButton floatingActionButton;
    private List<MainModel> offers;
    EditText getoff;
    TextInputLayout txtoff;
    String m;
    WifiApManager wifiApManager;

    String editTextAddress="192.168.43.1",editTextPort="1705";
    int delay=5000,i=0;
    Handler h;
    ArrayList<String> ippool;


    public static HomeFragment newInstance(int pageNo) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNo);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNo = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);
        rv=(RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        rv.setLayoutManager(llm);
        initializeData();
        initializeAdapter();
        final MainAdapter adapter = new MainAdapter(offers);
        rv.setAdapter(adapter);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater(savedInstanceState).inflate(R.layout.dialog_layout,null);
                getoff = (EditText) view.findViewById(R.id.dialog_text);
                txtoff =(TextInputLayout) view.findViewById(R.id.diail);
                alertDialog.setTitle("Offer Details");
                alertDialog.setView(view);
                alertDialog.setIcon(R.drawable.logo_140);
                alertDialog.setCancelable(false);

                alertDialog.setPositiveButton("Add Offer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String off = getoff.getText().toString();
                        if(off.isEmpty()){
                            Toast.makeText(getContext(), "Text can't be empty!", Toast.LENGTH_SHORT).show();
                            txtoff.setError("Your offer!");
                        }else {
                            offers.add(new MainModel(off,false));
                            txtoff.setErrorEnabled(false);

                        }
                    }
                });
                alertDialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                rv.setAdapter(adapter);

            }
        });
        rv.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), rv, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                final int pos=position;
                if(!offers.get(pos).getLive()) {

                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setPositiveButton(
                            "CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder1.setNegativeButton(
                            "PUSH",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                     m = offers.get(pos).getOfferf();;
                                    for (int i = 0; i < offers.size(); i++) {
                                        if (offers.get(i).getLive()) {
                                            offers.get(i).setLive(false);
                                        }
                                    }
                                    offers.get(pos).setLive(true);
                                    rv.setAdapter(adapter);
                                    ippool= new ArrayList<String>();
                                    wifiApManager = new WifiApManager(getContext());
                                    wifiApManager.setWifiApEnabled(null, true);
                                    h = new Handler();
                                    h.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            h.postDelayed(this, delay);
                                            scan();
                                        }
                                    }, delay);

                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = builder1.create();
                    alertDialog.setTitle("Make your offer LIVE!");
                    alertDialog.setMessage("Turns on the hotspots");
                    alertDialog.setIcon(R.drawable.logo_140);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setPositiveButton(
                            "CANCEL",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    builder1.setNegativeButton(
                            "TURN OFF",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    MainModel m = offers.get(pos);
                                    m.getOfferf();
                                    for (int i = 0; i < offers.size(); i++) {
                                        if (offers.get(i).getLive()) {
                                            offers.get(i).setLive(false);
                                        }
                                    }
                                    rv.setAdapter(adapter);

                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog = builder1.create();
                    alertDialog.setTitle("Make your offer LIVE!");
                    alertDialog.setMessage("Turns on the hotspots");
                    alertDialog.setIcon(R.drawable.logo_140);
                    alertDialog.setCanceledOnTouchOutside(false);
                    alertDialog.show();
                }

            }
            @Override
            public void onLongClick(View view, int position) {
                final int pos=position;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setPositiveButton(
                        "CANCEL",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int id) {
                                offers.remove(pos);
                                rv.setAdapter(adapter);
                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = builder1.create();
                alertDialog.setTitle("Make your offer LIVE!");
                alertDialog.setMessage("Turns on the hotspots");
                alertDialog.setIcon(R.drawable.logo_140);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();


            }
        }));
        return view;
    }

    private void initializeAdapter(){
        MainAdapter adapter = new MainAdapter(offers);
        rv.setAdapter(adapter);
    }
    private void initializeData(){
        offers = new ArrayList<>();
        offers.add(new MainModel("Amazing Recharge Offers on MyAirtel App\n" +
                "+ Upto 3.5% Cashback From CouponDunia ",true));
        offers.add(new MainModel("Get amazing offers on International Roaming Smartpicks. Offer is valid only for postpaid sims.",false));
        offers.add(new MainModel("Get amazing offers and promo codes on various transactions made via Airtel Money. Offer valid for prepaid recharges only.",false));
    }
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private HomeFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HomeFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    private void scan() {
        wifiApManager.getClientList(false, new FinishScanListener() {
            int f=0;
            @Override
            public void onFinishScan(final ArrayList<ClientScanResult> clients) {

                Toast.makeText(getContext(), ""+(i++), Toast.LENGTH_SHORT).show();
                for (ClientScanResult clientScanResult : clients) {
                    editTextAddress=clientScanResult.getIpAddr();
                    if(editTextAddress!=null )
                    {
                        if(ippool!=null)
                            for (String str:ippool)
                            {
                                if(str.equals(editTextAddress))
                                {
                                    f=1;
                                    break;
                                }
                            }

                        if(f==0){
                            ippool.add(editTextAddress);
                            HomeFragment.MyClientTask myClientTask = new HomeFragment.MyClientTask(editTextAddress, Integer.parseInt(editTextPort), m);
                            myClientTask.execute();

                        }}
                    f=0;
                }
            }
        });
    }
    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";
        String msgToServer;

        MyClientTask(String addr, int port, String msgTo) {
            dstAddress = addr;
            dstPort = port;
            msgToServer = msgTo;
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());

                if(msgToServer != null){
                    dataOutputStream.writeUTF(msgToServer);
                }

                response = dataInputStream.readUTF();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "IOException: " + e.toString();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

    }

}
