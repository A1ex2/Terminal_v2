package ua.org.algoritm.terminal.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ua.org.algoritm.terminal.Adapters.RecyclerAdapterDamage;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterEquipment;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterInspection;
import ua.org.algoritm.terminal.Adapters.RecyclerAdapterTypesPhoto;
import ua.org.algoritm.terminal.ConnectTo1c.FtpUtil;
import ua.org.algoritm.terminal.ConnectTo1c.SFTPClient;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Dispatcher;
import ua.org.algoritm.terminal.ConnectTo1c.SOAP_Objects;
import ua.org.algoritm.terminal.ConnectTo1c.UIManager;
import ua.org.algoritm.terminal.Constants;
import ua.org.algoritm.terminal.DataBase.SharedData;
import ua.org.algoritm.terminal.MainActivity;
import ua.org.algoritm.terminal.Objects.ActInspection;
import ua.org.algoritm.terminal.Objects.CarData;
import ua.org.algoritm.terminal.Objects.Damage;
import ua.org.algoritm.terminal.Objects.Equipment;
import ua.org.algoritm.terminal.Objects.Inspection;
import ua.org.algoritm.terminal.Objects.PhotoActInspection;
import ua.org.algoritm.terminal.Objects.TypeDamagePhoto;
import ua.org.algoritm.terminal.Objects.TypesPhoto;
import ua.org.algoritm.terminal.R;
import ua.org.algoritm.terminal.Service.IntentServiceDataBase;
import ua.org.algoritm.terminal.Service.IntentServicePerformedAct;
import ua.org.algoritm.terminal.Service.ServicePerformedAct;
import ua.org.algoritm.terminal.ViewAnimation;

public class ActInspectionActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_UPDATE_DAMAGE = 20;

    public static final int REQUEST_TAKE_PHOTO_Equipment = 1;
    public static final int REQUEST_TAKE_PHOTO_TypesPhoto = 2;
    public static final int REQUEST_UPDATE_PHOTO_TypesPhoto = 3;

    private static final int REQUEST_CODE_SCAN = 0x0000c0de;

    private ActInspection mActInspection;
    private CarData carData;

    private Calendar dateAndTime = Calendar.getInstance();

    private TextView itemForm;
    private TextView itemDatePlan;
    private TextView itemState;
    private TextView itemCar;
    private TextView itemBarCode;
    private TextView itemProductionDate;
    private TextView itemSector;
    private TextView itemRow;
    private TabHost tabHost;

    private FloatingActionButton addDamage;
    private FloatingActionButton fabDetail;
    private FloatingActionButton fabOther;

    private TextView textDetail;
    private TextView textOther;

    private boolean isRotate = false;
    private boolean performedAct = false;

    private String mCurrentPhotoPath;
    private Equipment mEquipment;
    private TypesPhoto mTypesPhoto;
    private int tabHostSelect = 0;

    private RecyclerView listInspection;
    private RecyclerAdapterInspection mAdapterInspection;

    private RecyclerView listEquipment;
    private RecyclerAdapterEquipment mAdapterEquipment;

    private RecyclerView listTypesPhoto;
    private RecyclerAdapterTypesPhoto mAdapterTypesPhoto;

    private RecyclerView listDamage;
    private RecyclerAdapterDamage mAdapterDamage;

    private ProgressDialog mDialog;
    private SaveTaskPhotoFTP mTaskPhotoFTP;

    private ImageButton vehicle_truck_position_01;
    private ImageButton vehicle_truck_position_02;
    private ImageButton vehicle_truck_position_03;
    private ImageButton vehicle_truck_position_04;
    private ImageButton vehicle_truck_position_05;
    private ImageButton vehicle_truck_position_06;
    private ImageButton vehicle_truck_position_07;
    private ImageButton vehicle_truck_position_08;
    private ImageButton vehicle_truck_position_09;
    private ImageButton vehicle_truck_position_10;

    private EditText run;

    private EditText barCode;
    private EditText editDate;
    private ImageButton mImageButtonScanBarCode;
    private ImageButton imButCalendar;

    public static final int ACTION_SET_ACT = 28;
    public static final int ACTION_SET_ACT_Performed = 29;
    public static final int ACTION_ConnectionError = 0;

    public static UIManager uiManager;
    public static SoapFault responseFault;

    public static SoapObject soapParam_Response;
    public static Handler soapHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_inspection);

        uiManager = new UIManager(this);
        soapHandler = new incomingHandler(this);

        Intent intent = getIntent();
        mActInspection = SharedData.getActInspection(intent.getStringExtra("actInspection"));
        carData = intent.getParcelableExtra("CarData");

        itemForm = findViewById(R.id.itemForm);
        itemDatePlan = findViewById(R.id.itemDatePlan);
        itemState = findViewById(R.id.itemState);
        itemCar = findViewById(R.id.itemCar);
//        itemBarCode = findViewById(R.id.itemBarCode);
//        itemProductionDate = findViewById(R.id.itemProductionDate);
//        itemSector = findViewById(R.id.itemSector);
//        itemRow = findViewById(R.id.itemRow);

        barCode = findViewById(R.id.barCode);
        editDate = findViewById(R.id.editDate);
        mImageButtonScanBarCode = findViewById(R.id.scanBarCode);
        imButCalendar = findViewById(R.id.imButCalendar);

        textDetail = findViewById(R.id.textDetail);
        textOther = findViewById(R.id.textOther);

        addDamage = findViewById(R.id.addDamage);
        fabDetail = findViewById(R.id.fabDetail);
        fabOther = findViewById(R.id.fabOther);

        ViewAnimation.init(fabDetail, textDetail);
        ViewAnimation.init(fabOther, textOther);

        run = findViewById(R.id.run);
        if (!mActInspection.getRun().equals("0")) {
            run.setText(mActInspection.getRun());
        }

        truckPosition();

        addDamage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRotate();
            }
        });

        fabDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRotate();

                Intent intent = new Intent(getApplicationContext(), DamageDetailActivity.class);
                intent.putExtra("actInspectionID", mActInspection.getID());
                intent.putExtra("idDamage", "");
                startActivityForResult(intent, REQUEST_CODE_UPDATE_DAMAGE);
            }
        });

        fabOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRotate();

                Intent intent = new Intent(getApplicationContext(), DamageDefectActivity.class);
                intent.putExtra("actInspectionID", mActInspection.getID());
                intent.putExtra("idDamage", "");
                startActivityForResult(intent, REQUEST_CODE_UPDATE_DAMAGE);
            }
        });

        listInspection = findViewById(R.id.list_inspection);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        listInspection.setLayoutManager(layoutManager);

        listEquipment = findViewById(R.id.list_equipment);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        listEquipment.setLayoutManager(layoutManager2);

        listTypesPhoto = findViewById(R.id.list_type_photo);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        listTypesPhoto.setLayoutManager(layoutManager3);

        listDamage = findViewById(R.id.list_damage);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this);
        listDamage.setLayoutManager(layoutManager4);

        itemForm.setText(mActInspection.getForm());
        itemDatePlan.setText(mActInspection.getInspectionDatePlanString());
        itemState.setText(mActInspection.getState());
        itemCar.setText(mActInspection.getCar());
//        itemBarCode.setText(mActInspection.getBarCode());
//        itemProductionDate.setText(mActInspection.getProductionDate());
//        itemSector.setText(mActInspection.getSector());
//        itemRow.setText(mActInspection.getRow());

//        ActionBar mbar = getSupportActionBar();
//        mbar.setTitle(mActInspection.getDescription());
//        mbar.setDisplayHomeAsUpEnabled(true);

        setTitle(mActInspection.getDescription());
        setTabHost();

        updateListInspections();
        updateListEquipments();
        updateListTypesPhoto();
        updateListDamage();

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int fromPos = viewHolder.getAdapterPosition();

//                mDialog = new ProgressDialog(ActInspectionActivity.this);
//                mDialog.setMessage(getString(R.string.wait));
//                mDialog.setCancelable(false);
//                mDialog.show();

                Damage mDamage = mActInspection.getDamages().get(fromPos);

                mActInspection.getDamages().remove(mDamage);
                updateListDamage();

                if (mDamage.getDetail() != null) {
                    IntentServiceDataBase.startDeletePhotoActInspectionDamage(ActInspectionActivity.this, mActInspection.getID(), mDamage.getDetail().getDetailID());
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(listDamage);

        if (carData != null) {
            barCode.setText(carData.getBarCode());
            editDate.setText(carData.getProductionDateString());

            Date date = carData.getProductionDate();
            if (date.getTime() > 0) {
                dateAndTime.setTime(date);
            }

            editDate.addTextChangedListener(getTextWatcher());
            editDate.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                editSector.performClick();
//                return true;
                    return false;
                }
            });

            mImageButtonScanBarCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanBarCode();
                }
            });

            imButCalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(ActInspectionActivity.this, d,
                            dateAndTime.get(Calendar.YEAR),
                            dateAndTime.get(Calendar.MONTH),
                            dateAndTime.get(Calendar.DAY_OF_MONTH))
                            .show();

                }
            });
        } else {
            LinearLayout groupEdit = findViewById(R.id.groupEdit);
            groupEdit.setVisibility(View.INVISIBLE);
        }
    }

    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();

        }
    };

    private TextWatcher getTextWatcher() {
        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s.%s.%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editDate.setText(current);
                    editDate.setSelection(sel < current.length() ? sel : current.length());

                    try {

                        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                        Date date = format.parse(current);
                        dateAndTime.setTime(date);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        return tw;
    }

    private void setInitialDateTime() {
        carData.setProductionDate(dateAndTime.getTime());
        editDate.setText(carData.getProductionDateString());
//        editSector.performClick();
    }

    private void scanBarCode() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(ScannerActivity.class);
        intentIntegrator.setDesiredBarcodeFormats(intentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setBeepEnabled(false);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setPrompt(getString(R.string.camera_to_the_barcode));
        intentIntegrator.setBarcodeImageEnabled(false);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
    }

    private void truckPosition() {
        vehicle_truck_position_01 = findViewById(R.id.vehicle_truck_position_01);
        vehicle_truck_position_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActInspection.getTruckPositionDirection().equals("direct")) {
                    vehicle_truck_position_01.setImageResource(R.mipmap.autotruck_car_reverse);
                    mActInspection.setTruckPositionDirection("reverse");
                } else {
                    vehicle_truck_position_01.setImageResource(R.mipmap.autotruck_car_direct);
                    mActInspection.setTruckPositionDirection("direct");
                }

                mActInspection.setTruckPosition(1);

                vehicle_truck_position_02.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_03.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_04.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_05.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_06.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_07.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_08.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_09.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_10.setImageResource(R.mipmap.autotruck_car);
            }
        });

        vehicle_truck_position_02 = findViewById(R.id.vehicle_truck_position_02);
        vehicle_truck_position_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActInspection.getTruckPositionDirection().equals("direct")) {
                    vehicle_truck_position_02.setImageResource(R.mipmap.autotruck_car_reverse);
                    mActInspection.setTruckPositionDirection("reverse");
                } else {
                    vehicle_truck_position_02.setImageResource(R.mipmap.autotruck_car_direct);
                    mActInspection.setTruckPositionDirection("direct");
                }

                mActInspection.setTruckPosition(2);

                vehicle_truck_position_01.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_03.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_04.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_05.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_06.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_07.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_08.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_09.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_10.setImageResource(R.mipmap.autotruck_car);
            }
        });

        vehicle_truck_position_03 = findViewById(R.id.vehicle_truck_position_03);
        vehicle_truck_position_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActInspection.getTruckPositionDirection().equals("direct")) {
                    vehicle_truck_position_03.setImageResource(R.mipmap.autotruck_car_reverse);
                    mActInspection.setTruckPositionDirection("reverse");
                } else {
                    vehicle_truck_position_03.setImageResource(R.mipmap.autotruck_car_direct);
                    mActInspection.setTruckPositionDirection("direct");
                }

                mActInspection.setTruckPosition(3);

                vehicle_truck_position_01.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_02.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_04.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_05.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_06.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_07.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_08.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_09.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_10.setImageResource(R.mipmap.autotruck_car);

            }
        });

        vehicle_truck_position_04 = findViewById(R.id.vehicle_truck_position_04);
        vehicle_truck_position_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActInspection.getTruckPositionDirection().equals("direct")) {
                    vehicle_truck_position_04.setImageResource(R.mipmap.autotruck_car_reverse);
                    mActInspection.setTruckPositionDirection("reverse");
                } else {
                    vehicle_truck_position_04.setImageResource(R.mipmap.autotruck_car_direct);
                    mActInspection.setTruckPositionDirection("direct");
                }

                mActInspection.setTruckPosition(4);

                vehicle_truck_position_01.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_02.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_03.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_05.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_06.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_07.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_08.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_09.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_10.setImageResource(R.mipmap.autotruck_car);
            }
        });

        vehicle_truck_position_05 = findViewById(R.id.vehicle_truck_position_05);
        vehicle_truck_position_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActInspection.getTruckPositionDirection().equals("direct")) {
                    vehicle_truck_position_05.setImageResource(R.mipmap.autotruck_car_reverse);
                    mActInspection.setTruckPositionDirection("reverse");
                } else {
                    vehicle_truck_position_05.setImageResource(R.mipmap.autotruck_car_direct);
                    mActInspection.setTruckPositionDirection("direct");
                }

                mActInspection.setTruckPosition(5);

                vehicle_truck_position_01.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_02.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_03.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_04.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_06.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_07.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_08.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_09.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_10.setImageResource(R.mipmap.autotruck_car);
            }
        });

        vehicle_truck_position_06 = findViewById(R.id.vehicle_truck_position_06);
        vehicle_truck_position_06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActInspection.getTruckPositionDirection().equals("direct")) {
                    vehicle_truck_position_06.setImageResource(R.mipmap.autotruck_car_reverse);
                    mActInspection.setTruckPositionDirection("reverse");
                } else {
                    vehicle_truck_position_06.setImageResource(R.mipmap.autotruck_car_direct);
                    mActInspection.setTruckPositionDirection("direct");
                }

                mActInspection.setTruckPosition(6);

                vehicle_truck_position_01.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_02.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_03.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_04.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_05.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_07.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_08.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_09.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_10.setImageResource(R.mipmap.autotruck_car);
            }
        });

        vehicle_truck_position_07 = findViewById(R.id.vehicle_truck_position_07);
        vehicle_truck_position_07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActInspection.getTruckPositionDirection().equals("direct")) {
                    vehicle_truck_position_07.setImageResource(R.mipmap.autotruck_car_reverse);
                    mActInspection.setTruckPositionDirection("reverse");
                } else {
                    vehicle_truck_position_07.setImageResource(R.mipmap.autotruck_car_direct);
                    mActInspection.setTruckPositionDirection("direct");
                }

                mActInspection.setTruckPosition(7);

                vehicle_truck_position_01.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_02.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_03.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_04.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_05.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_06.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_08.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_09.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_10.setImageResource(R.mipmap.autotruck_car);
            }
        });

        vehicle_truck_position_08 = findViewById(R.id.vehicle_truck_position_08);
        vehicle_truck_position_08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActInspection.getTruckPositionDirection().equals("direct")) {
                    vehicle_truck_position_08.setImageResource(R.mipmap.autotruck_car_reverse);
                    mActInspection.setTruckPositionDirection("reverse");
                } else {
                    vehicle_truck_position_08.setImageResource(R.mipmap.autotruck_car_direct);
                    mActInspection.setTruckPositionDirection("direct");
                }

                mActInspection.setTruckPosition(8);

                vehicle_truck_position_01.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_02.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_03.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_04.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_05.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_06.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_07.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_09.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_10.setImageResource(R.mipmap.autotruck_car);
            }
        });

        vehicle_truck_position_09 = findViewById(R.id.vehicle_truck_position_09);
        vehicle_truck_position_09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActInspection.getTruckPositionDirection().equals("direct")) {
                    vehicle_truck_position_09.setImageResource(R.mipmap.autotruck_car_reverse);
                    mActInspection.setTruckPositionDirection("reverse");
                } else {
                    vehicle_truck_position_09.setImageResource(R.mipmap.autotruck_car_direct);
                    mActInspection.setTruckPositionDirection("direct");
                }

                mActInspection.setTruckPosition(9);

                vehicle_truck_position_01.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_02.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_03.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_04.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_05.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_06.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_07.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_08.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_10.setImageResource(R.mipmap.autotruck_car);
            }
        });

        vehicle_truck_position_10 = findViewById(R.id.vehicle_truck_position_10);
        vehicle_truck_position_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mActInspection.getTruckPositionDirection().equals("direct")) {
                    vehicle_truck_position_10.setImageResource(R.mipmap.autotruck_car_reverse);
                    mActInspection.setTruckPositionDirection("reverse");
                } else {
                    vehicle_truck_position_10.setImageResource(R.mipmap.autotruck_car_direct);
                    mActInspection.setTruckPositionDirection("direct");
                }

                mActInspection.setTruckPosition(10);

                vehicle_truck_position_01.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_02.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_03.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_04.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_05.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_06.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_07.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_08.setImageResource(R.mipmap.autotruck_car);
                vehicle_truck_position_09.setImageResource(R.mipmap.autotruck_car);
            }
        });

        if (mActInspection.getTruckPosition() == 1) {
            setTruckPosition(vehicle_truck_position_01);

        } else if (mActInspection.getTruckPosition() == 2) {
            setTruckPosition(vehicle_truck_position_02);

        } else if (mActInspection.getTruckPosition() == 3) {
            setTruckPosition(vehicle_truck_position_03);

        } else if (mActInspection.getTruckPosition() == 4) {
            setTruckPosition(vehicle_truck_position_04);

        } else if (mActInspection.getTruckPosition() == 5) {
            setTruckPosition(vehicle_truck_position_05);

        } else if (mActInspection.getTruckPosition() == 6) {
            setTruckPosition(vehicle_truck_position_06);

        } else if (mActInspection.getTruckPosition() == 7) {
            setTruckPosition(vehicle_truck_position_07);

        } else if (mActInspection.getTruckPosition() == 8) {
            setTruckPosition(vehicle_truck_position_08);

        } else if (mActInspection.getTruckPosition() == 9) {
            setTruckPosition(vehicle_truck_position_09);

        } else if (mActInspection.getTruckPosition() == 10) {
            setTruckPosition(vehicle_truck_position_10);
        }
    }

    private void setTruckPosition(ImageButton vehicle_truck) {
        if (mActInspection.getTruckPositionDirection().equals("reverse")) {
            vehicle_truck.setImageResource(R.mipmap.autotruck_car_reverse);
        } else {
            vehicle_truck.setImageResource(R.mipmap.autotruck_car_direct);
        }
    }

    private void setRotate() {
        isRotate = ViewAnimation.rotateFab(addDamage, !isRotate);
        if (isRotate) {
            ViewAnimation.showIn(fabDetail, textDetail);
            ViewAnimation.showIn(fabOther, textOther);
        } else {
            ViewAnimation.showOut(fabDetail, textDetail);
            ViewAnimation.showOut(fabOther, textOther);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_db_act_inspection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveCB:
                setCB();
                return true;

            case R.id.savePerformed:
                String message = getString(R.string.send_performed);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.butt_Yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                performedAct = true;
                                setCB();
                            }
                        })
                        .setNegativeButton(getString(R.string.butt_Not), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCB() {
        if (carData != null) {
            carData.setBarCode(barCode.getText().toString());
            if (!editDate.getText().toString().equals("")) {
                carData.setProductionDate(dateAndTime.getTime());
            }
            mActInspection.setBarCode(carData.getBarCode());
            mActInspection.setProductionDate(carData.getProductionDateString());
        }

        mActInspection.setRun(run.getText().toString());

        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.wait_sending));
        mDialog.setCancelable(false);
        mDialog.show();

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String login = preferences.getString("Login", "");
        String password = preferences.getString("Password", "");

        //mActInspection.setPerformed(performed);

        String stringObject = SOAP_Objects.getActInspection(mActInspection);

        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_SET_ACT, login, password, getApplicationContext());
        dispatcher.string_Inquiry = stringObject;

        dispatcher.start();
    }

    private void setCBPerformed(boolean performed) {
        mActInspection.setRun(run.getText().toString());

        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getString(R.string.wait_sending));
        mDialog.setCancelable(false);
        mDialog.show();

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        String login = preferences.getString("Login", "");
        String password = preferences.getString("Password", "");

        mActInspection.setPerformed(performed);

        String stringObject = SOAP_Objects.getActInspection(mActInspection);

        SOAP_Dispatcher dispatcher = new SOAP_Dispatcher(ACTION_SET_ACT_Performed, login, password, getApplicationContext());
        dispatcher.string_Inquiry = stringObject;

        dispatcher.start();
    }

    private void updateListTypesPhoto() {
        if (mAdapterTypesPhoto == null) {
            mAdapterTypesPhoto = new RecyclerAdapterTypesPhoto(this, R.layout.item_photo_act, mActInspection.getTypesPhotos());
            listTypesPhoto.setAdapter(mAdapterTypesPhoto);
            mAdapterTypesPhoto.setActionListener(new RecyclerAdapterTypesPhoto.ActionListener() {
                @Override
                public void onClicViewPhotos(TypesPhoto typesPhoto) {
                    Intent i = new Intent(getApplicationContext(), ListActPhoto.class);
                    i.putExtra("actInspectionID", mActInspection.getID());
                    i.putExtra("typesPhotoID", typesPhoto.getTypePhotoID());
                    startActivityForResult(i, REQUEST_UPDATE_PHOTO_TypesPhoto);
                }

                @Override
                public void onClicBtnAdd(TypesPhoto typesPhoto) {
                    mTypesPhoto = typesPhoto;
                    dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_TypesPhoto);
                }
            });
        } else {
            mAdapterTypesPhoto.notifyDataSetChanged();
        }
    }

    private void updateListInspections() {
        if (mAdapterInspection == null) {
            mAdapterInspection = new RecyclerAdapterInspection(this, R.layout.item_inspection, mActInspection.getInspections());
            listInspection.setAdapter(mAdapterInspection);
            mAdapterInspection.setActionListener(new RecyclerAdapterInspection.ActionListener() {
                @Override
                public void onClick(Inspection inspection) {
                    inspection.setPerformed(!inspection.isPerformed());
                    mAdapterInspection.notifyDataSetChanged();
                }
            });
        } else {
            mAdapterInspection.notifyDataSetChanged();
        }
    }

    private void updateListDamage() {
        if (mAdapterDamage == null) {
            mAdapterDamage = new RecyclerAdapterDamage(this, R.layout.item_damage, mActInspection.getDamages());
            listDamage.setAdapter(mAdapterDamage);
            mAdapterDamage.setActionListener(new RecyclerAdapterDamage.ActionListener() {
                @Override
                public void onClick(Damage damage) {

                    Intent intent = new Intent(getApplicationContext(), DamageDetailActivity.class);
                    if (damage.getTypeDetail().equals("defect")) {
                        intent = new Intent(getApplicationContext(), DamageDefectActivity.class);
                    }

                    intent.putExtra("actInspectionID", mActInspection.getID());

                    if (damage.getDetail() == null) {
                        intent.putExtra("idDamage", "null");
                    } else {
                        intent.putExtra("idDamage", damage.getDetail().getDetailID());
                    }

                    startActivityForResult(intent, REQUEST_CODE_UPDATE_DAMAGE);
                }
            });
        } else {
            mAdapterDamage.setDamage(mActInspection.getDamages());
            mAdapterDamage.notifyDataSetChanged();
        }
    }

    private void updateListEquipments() {
        if (mAdapterEquipment == null) {
            mAdapterEquipment = new RecyclerAdapterEquipment(this, R.layout.item_equipment, mActInspection.getEquipments());
            listEquipment.setAdapter(mAdapterEquipment);
            mAdapterEquipment.setActionListener(new RecyclerAdapterEquipment.ActionListener() {
                @Override
                public void onClicViewPhoto(Equipment equipment) {
                    mEquipment = equipment;
                    dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_Equipment);
                }

                @Override
                public void onClicBtnAdd(Equipment equipment) {
                    equipment.setQuantityFact(equipment.getQuantityFact() + 1);
                    updateListEquipments();
                }

                @Override
                public void onClicBtnSub(Equipment equipment) {
                    equipment.setQuantityFact(equipment.getQuantityFact() - 1);
                    updateListEquipments();
                }

                @Override
                public void onClicBtnEqually(Equipment equipment) {
                    equipment.setQuantityFact(equipment.getQuantityPlan());
                    updateListEquipments();
                }
            });
        } else {
            mAdapterEquipment.notifyDataSetChanged();
        }
    }

    private void setQuantityFactEquipments(Equipment equipment) {
        for (int i = 0; i < mActInspection.getEquipments().size(); i++) {
            if (mActInspection.getEquipments().get(i).getEquipmentID().equals(equipment.getEquipmentID())) {
                mActInspection.getEquipments().get(i).setQuantityFact(equipment.getQuantityFact());
                break;
            }
        }
    }

    private void setTabHost() {
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.linearLayoutInfo);
        tabSpec.setIndicator(getString(R.string.text_info));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.linearLayoutInspection);
        tabSpec.setIndicator(getString(R.string.text_inspection));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setContent(R.id.linearLayoutPhoto);
        tabSpec.setIndicator(getString(R.string.text_photo));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag4");
        tabSpec.setContent(R.id.linearLayoutEquipment);
        tabSpec.setIndicator(getString(R.string.text_complete));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag5");
        tabSpec.setContent(R.id.linearLayoutDamage);
        tabSpec.setIndicator(getString(R.string.text_damage));
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mCurrentPhotoPath", mCurrentPhotoPath);
        outState.putSerializable("mEquipment", mEquipment);
        outState.putSerializable("mTypesPhoto", mTypesPhoto);

        tabHostSelect = tabHost.getCurrentTab();
        outState.putInt("tabHostSelect", tabHostSelect);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPhotoPath = savedInstanceState.getString("mCurrentPhotoPath");
        mEquipment = (Equipment) savedInstanceState.getSerializable("mEquipment");
        mTypesPhoto = (TypesPhoto) savedInstanceState.getSerializable("mTypesPhoto");

        tabHostSelect = savedInstanceState.getInt("tabHostSelect", 0);
        tabHost.setCurrentTab(tabHostSelect);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO_Equipment && resultCode == RESULT_OK) {

            if (!mEquipment.getPhotoActInspection().getCurrentPhotoPath().equals("")) {
                SharedData.deletePhotoActInspection(mEquipment.getPhotoActInspection().getCurrentPhotoPath());
            }

            String fileName = new File(mCurrentPhotoPath).getName();
            PhotoActInspection photoActInspection = new PhotoActInspection();
            photoActInspection.setActID(mActInspection.getID());
            photoActInspection.setName(fileName);
            photoActInspection.setObjectID(mEquipment.getEquipmentID());
            photoActInspection.setListObject(mEquipment.getListObject());
            photoActInspection.setCurrentPhotoPath(mCurrentPhotoPath);

            mEquipment.setPhotoActInspection(photoActInspection);

            IntentServiceDataBase.startInsertPhotoActInspection(ActInspectionActivity.this,
                    mActInspection.getID(), mEquipment.getListObject(), mEquipment.getEquipmentID(), mCurrentPhotoPath);

            decodeFile();

            mCurrentPhotoPath = "";
            mEquipment = null;
            updateListEquipments();

        } else if (requestCode == REQUEST_TAKE_PHOTO_TypesPhoto && resultCode == RESULT_OK) {

            String fileName = new File(mCurrentPhotoPath).getName();

            PhotoActInspection photoActInspection = new PhotoActInspection();
            photoActInspection.setActID(mActInspection.getID());
            photoActInspection.setName(fileName);
            photoActInspection.setObjectID(mTypesPhoto.getTypePhotoID());
            photoActInspection.setListObject(mTypesPhoto.getListObject());
            photoActInspection.setCurrentPhotoPath(mCurrentPhotoPath);

            mTypesPhoto.getPhotoActInspections().add(photoActInspection);

            IntentServiceDataBase.startInsertPhotoActInspection(ActInspectionActivity.this,
                    mActInspection.getID(), mTypesPhoto.getListObject(), mTypesPhoto.getTypePhotoID(), mCurrentPhotoPath);

            decodeFile();

            Toast.makeText(this, mTypesPhoto.getTypePhoto(), Toast.LENGTH_SHORT).show();

            mCurrentPhotoPath = "";

            String message = getString(R.string.add_photo);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.butt_next), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            for (int i = 0; i < mActInspection.getTypesPhotos().size(); i++) {
                                if (mTypesPhoto.getTypePhotoID().equals(mActInspection.getTypesPhotos().get(i).getTypePhotoID())) {
                                    if (i >= mActInspection.getTypesPhotos().size() - 1) {
                                        Toast.makeText(getApplicationContext(), getString(R.string.all_photos_added), Toast.LENGTH_SHORT).show();
                                        mTypesPhoto = null;
                                        updateListTypesPhoto();

                                    } else {
                                        mTypesPhoto = mActInspection.getTypesPhotos().get(i + 1);
                                        dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_TypesPhoto);
                                    }

                                    break;
                                }
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.butt_more_photos), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_TypesPhoto);
                            updateListTypesPhoto();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        } else if ((requestCode == REQUEST_TAKE_PHOTO_Equipment | requestCode == REQUEST_TAKE_PHOTO_TypesPhoto) && resultCode == RESULT_CANCELED) {
            SharedData.deletePhotoActInspection(mCurrentPhotoPath);

        } else if (requestCode == REQUEST_UPDATE_PHOTO_TypesPhoto) {
            updateListTypesPhoto();

        } else if (requestCode == REQUEST_CODE_UPDATE_DAMAGE) {
            updateListDamage();

        } else if (requestCode == REQUEST_CODE_SCAN) {
            IntentResult Result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (Result != null) {
                if (Result.getContents() == null) {

                } else {
                    String tBarCode = Result.getContents();
                    tBarCode = SharedData.clearBarcode(tBarCode);
                    barCode.setText(tBarCode);
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent(int REQUEST) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI;
                photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST);
            }
        }
    }

    private void decodeFile() {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            FileOutputStream fos = null;
            try {
                File file = new File(mCurrentPhotoPath);
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            } finally {
                if (fos != null) fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class incomingHandler extends Handler {
        private final WeakReference<ActInspectionActivity> mTarget;

        public incomingHandler(ActInspectionActivity context) {
            mTarget = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            ActInspectionActivity target = mTarget.get();

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            switch (msg.what) {
                case ACTION_ConnectionError:
                    uiManager.showToast(getString(R.string.errorConnection) + getSoapErrorMessage());
                    break;

                case ACTION_SET_ACT: {
                    target.checkSetAct();
                    break;
                }

                case ACTION_SET_ACT_Performed: {
                    target.isOK();
                }
            }
        }
    }

    private void isOK() {
        Boolean isSaveSuccess = Boolean.parseBoolean(soapParam_Response.getPropertyAsString("Result"));
        if (isSaveSuccess) {
            finishActivity();
        } else {
            uiManager.showToast(soapParam_Response.getPropertyAsString("Description"));
        }
    }

    private void checkSetAct() {
        Boolean isSaveSuccess = Boolean.parseBoolean(soapParam_Response.getPropertyAsString("Result"));

        if (isSaveSuccess) {
            final ArrayList<PhotoActInspection> photoAll = new ArrayList<>();

            for (int i = 0; i < mActInspection.getEquipments().size(); i++) {
                if (!mActInspection.getEquipments().get(i).getPhotoActInspection().getCurrentPhotoPath().equals("")) {
                    photoAll.add(mActInspection.getEquipments().get(i).getPhotoActInspection());
                }
            }

            for (int i = 0; i < mActInspection.getTypesPhotos().size(); i++) {
                TypesPhoto typesPhoto = mActInspection.getTypesPhotos().get(i);
                for (int j = 0; j < typesPhoto.getPhotoActInspections().size(); j++) {
                    if (!typesPhoto.getPhotoActInspections().get(j).getCurrentPhotoPath().equals("")) {
                        photoAll.add(typesPhoto.getPhotoActInspections().get(j));
                    }
                }
            }

            for (int i = 0; i < mActInspection.getDamages().size(); i++) {
                for (int j = 0; j < mActInspection.getDamages().get(i).getTypeDamagePhoto().size(); j++) {
                    TypeDamagePhoto typesPhoto = mActInspection.getDamages().get(i).getTypeDamagePhoto().get(j);

                    for (int k = 0; k < typesPhoto.getPhotoActInspections().size(); k++) {
                        if (!typesPhoto.getPhotoActInspections().get(k).getCurrentPhotoPath().equals("")) {
                            photoAll.add(typesPhoto.getPhotoActInspections().get(k));
                        }
                    }
                }
            }

            if (photoAll.size() != 0) {
                uiManager.showToast(getString(R.string.success));
                String message = getString(R.string.send_photo);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(message)
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.butt_Yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent startIntent = new Intent(ActInspectionActivity.this, ServicePerformedAct.class);
                                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                                startIntent.putExtra(ServicePerformedAct.EXTRA_ACT_ID, mActInspection.getID());
                                if (performedAct) {
                                    startIntent.putExtra(ServicePerformedAct.EXTRA_performedAct, performedAct);
//                                    setCBPerformed(true);
                                } else {
                                }
                                startService(startIntent);

//                                mTaskPhotoFTP = new SaveTaskPhotoFTP(ActInspectionActivity.this, mActInspection.getID());
//                                mTaskPhotoFTP.execute(photoAll);
                                finishActivity();
                            }
                        })
                        .setNegativeButton(getString(R.string.butt_Not), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                finish();

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                if (performedAct) {
                    performedAct = false;
                    String message = getString(R.string.no_photos);

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(message)
                            .setCancelable(true)
                            .setPositiveButton(getString(R.string.butt_OK), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

//                    setCBPerformed(true);
                } else {
                    finishActivity();
                }
            }

        } else {
            uiManager.showToast(soapParam_Response.getPropertyAsString("Description"));
        }
    }

    private void finishActivity() {
        Intent intent = new Intent();
        intent.putExtra("performedAct", performedAct);

        if (carData != null) {
            intent.putExtra("CarData", carData);
        }

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private String getSoapErrorMessage() {

        String errorMessage;

        if (responseFault == null)
            errorMessage = getString(R.string.textNoInternet);
        else {
            try {
                errorMessage = responseFault.faultstring;
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage = getString(R.string.unknownError);
            }
        }
        return errorMessage;
    }

    public class SaveTaskPhotoFTP extends AsyncTask<ArrayList<PhotoActInspection>, Integer, Boolean> {
        private Context mContext;
        private ProgressDialog mDialog;
        private String actID;
        private boolean error;

        public SaveTaskPhotoFTP(Context context, String actID) {
            this.mContext = context;
            this.actID = actID;
            this.error = false;
        }

        @Override
        protected Boolean doInBackground(ArrayList<PhotoActInspection>... arrayLists) {
            for (ArrayList<PhotoActInspection> mPhotos : arrayLists) {
                for (int i = 0; i < mPhotos.size(); i++) {
                    PhotoActInspection photo = mPhotos.get(i);
                    publishProgress(mPhotos.size(), i + 1);

                    if (sendPhoto(photo)) {
                        //SharedData.deletePhoto(photo.getCurrentPhotoPath());
                    } else {
                        error = true;
                        break;
                    }
                }
            }
            return null;
        }

        private boolean sendPhoto(PhotoActInspection photo) {
            boolean uploadFile = false;
            if (!photo.getCurrentPhotoPathFTP().equals("")) {
                uploadFile = true;
                return uploadFile;
            }

            try {

                String host = SharedData.hostFTP;
                int port = SharedData.portFTP;
                String username = SharedData.usernameFTP;
                String password = SharedData.passwordFTP;
                boolean thisSFTP = SharedData.thisSFTP;

                String basePath = "";
                String filePath = "" + actID + "/" + photo.getListObject();
                if (photo.getListObject().equals("DamagePhoto")) {
                    filePath = filePath + "/" + photo.getObjectID();
                }
                String filename = photo.getName();

                if (thisSFTP) {
                    SFTPClient sftpClient = new SFTPClient(host, username, password, port);
                    sftpClient.connect();
                    try {
                        sftpClient.upload(photo.getCurrentPhotoPath(), "foto/" + filePath, photo.getName());
                        uploadFile = true;
                    } catch (Exception e) {
                        uploadFile = false;
                    } finally {
                        sftpClient.disconnect();
                    }
                } else {
                    InputStream input = new FileInputStream(new File(photo.getCurrentPhotoPath()));
                    uploadFile = FtpUtil.uploadFile(host, port, username, password, basePath, filePath, filename, input);
                }
            } catch (Exception e) {
                uploadFile = false;
            }
            return uploadFile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(mContext);
            mDialog.setMessage(mContext.getResources().getString(R.string.wait_ftp));
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mDialog.setMessage(String.format(mContext.getResources().getString(R.string.send_ftp), values[1], values[0]));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            if (error) {
                Toast.makeText(mContext, R.string.error_ftp, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, R.string.ok_ftp, Toast.LENGTH_LONG).show();

                if (performedAct) {
                    setCBPerformed(true);
                } else {
                    finishActivity();
                }
            }
        }
    }

}

