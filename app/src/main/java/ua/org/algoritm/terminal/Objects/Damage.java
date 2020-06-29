package ua.org.algoritm.terminal.Objects;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

import ua.org.algoritm.terminal.DataBase.SharedData;

public class Damage implements Serializable {
    @Expose
    private Detail detail;

    @Expose
    private TypeDamage mTypeDamage;

    @Expose
    private String typeDetail;

    @Expose
    private DegreesDamage mDegreesDamage;

    @Expose
    private ClassificationDamage mClassificationDamage;

    @Expose
    private OriginDamage mOriginDamage;

    @Expose
    private String detailDamage;

    @Expose
    private String commentDamage;
    @Expose
    private String widthDamage;
    @Expose
    private String heightDamage;

    @Expose
    private ArrayList<TypeDamagePhoto> TypeDamagePhoto = new ArrayList<>();

    public Damage() {
        for (int i = 0; i < SharedData.TypeDamagePhotos.size(); i++) {
            getTypeDamagePhoto().add(new TypeDamagePhoto(SharedData.TypeDamagePhotos.get(i)));
        }
    }

    public void copyDamage(Damage mDamage) {
        this.detail = mDamage.getDetail();
        this.mTypeDamage = mDamage.getTypeDamage();
        this.mDegreesDamage = mDamage.getDegreesDamage();
        this.mClassificationDamage = mDamage.getClassificationDamage();
        this.mOriginDamage = mDamage.getOriginDamage();
        this.detailDamage = mDamage.getDetailDamage();
        this.commentDamage = mDamage.getCommentDamage();
        this.widthDamage = mDamage.getWidthDamage();
        this.heightDamage = mDamage.getHeightDamage();
        this.TypeDamagePhoto = mDamage.getTypeDamagePhoto();
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public TypeDamage getTypeDamage() {
        return mTypeDamage;
    }

    public void setTypeDamage(TypeDamage typeDamage) {
        mTypeDamage = typeDamage;
    }

    public String getDetailDamage() {
        return detailDamage;
    }

    public void setDetailDamage(String detailDamage) {
        this.detailDamage = detailDamage;
    }

    public String getCommentDamage() {
        return commentDamage;
    }

    public void setCommentDamage(String commentDamage) {
        this.commentDamage = commentDamage;
    }

    public ArrayList<ua.org.algoritm.terminal.Objects.TypeDamagePhoto> getTypeDamagePhoto() {
        return TypeDamagePhoto;
    }

    public void setTypeDamagePhoto(ArrayList<ua.org.algoritm.terminal.Objects.TypeDamagePhoto> typeDamagePhoto) {
        TypeDamagePhoto = typeDamagePhoto;
    }

    public DegreesDamage getDegreesDamage() {
        return mDegreesDamage;
    }

    public void setDegreesDamage(DegreesDamage degreesDamage) {
        mDegreesDamage = degreesDamage;
    }

    public ClassificationDamage getClassificationDamage() {
        return mClassificationDamage;
    }

    public void setClassificationDamage(ClassificationDamage classificationDamage) {
        mClassificationDamage = classificationDamage;
    }

    public OriginDamage getOriginDamage() {
        return mOriginDamage;
    }

    public void setOriginDamage(OriginDamage originDamage) {
        mOriginDamage = originDamage;
    }

    public String getWidthDamage() {
        return widthDamage;
    }

    public void setWidthDamage(String widthDamage) {
        this.widthDamage = widthDamage;
    }

    public String getHeightDamage() {
        return heightDamage;
    }

    public void setHeightDamage(String heightDamage) {
        this.heightDamage = heightDamage;
    }

    public String getTypeDetail() {
        return typeDetail;
    }

    public void setTypeDetail(String typeDetail) {
        this.typeDetail = typeDetail;
    }

    public void setDetail(String typeMachineID, String detailID) {
        if (detailID.equals("")) {

        } else {
            for (int i = 0; i < SharedData.SCHEMES.size(); i++) {
                if (SharedData.SCHEMES.get(i).getTypeMachineID().equals(typeMachineID)) {
                    for (int j = 0; j < SharedData.SCHEMES.get(i).getDetails().size(); j++) {
                        Detail mDetail = SharedData.SCHEMES.get(i).getDetails().get(j);
                        if (mDetail.getDetailID().equals(detailID)) {
                            this.detail = mDetail;
                            return;
                        }
                    }
                }
            }

            for (int i = 0; i < SharedData.DamageDefect.size(); i++) {
                Detail mDetail = SharedData.DamageDefect.get(i);
                if (mDetail.getDetailID().equals(detailID)) {
                    this.detail = mDetail;
                    return;
                }
            }

        }
    }

    public void setTypeDamage(String mTypeDamageID) {
        if (mTypeDamageID.equals("")) {

        } else {
            for (int i = 0; i < SharedData.TypesDamages.size(); i++) {
                TypeDamage typeDamage = SharedData.TypesDamages.get(i);
                if (typeDamage.getID().equals(mTypeDamageID)) {
                    this.mTypeDamage = typeDamage;
                    break;
                }
            }
        }
    }

    public void setDegreesDamage(String mDegreesDamageID) {
        if (mDegreesDamageID.equals("")) {

        } else {
            for (int i = 0; i < SharedData.DegreesDamages.size(); i++) {
                DegreesDamage degreesDamage = SharedData.DegreesDamages.get(i);
                if (degreesDamage.getID().equals(mDegreesDamageID)) {
                    this.mDegreesDamage = degreesDamage;
                    break;
                }
            }
        }
    }

    public void setClassificationDamage(String mClassificationDamageID) {
        if (mClassificationDamageID.equals("")) {

        } else {
            for (int i = 0; i < SharedData.ClassificationDamages.size(); i++) {
                ClassificationDamage classificationDamage = SharedData.ClassificationDamages.get(i);
                if (classificationDamage.getID().equals(mClassificationDamageID)) {
                    this.mClassificationDamage = classificationDamage;
                    break;
                }
            }
        }
    }

    public void setOriginDamage(String mOriginDamageID) {
        if (mOriginDamageID.equals("")) {

        } else {
            for (int i = 0; i < SharedData.OriginDamages.size(); i++) {
                OriginDamage originDamage = SharedData.OriginDamages.get(i);
                if (originDamage.getID().equals(mOriginDamageID)) {
                    this.mOriginDamage = originDamage;
                    break;
                }
            }
        }
    }
}
