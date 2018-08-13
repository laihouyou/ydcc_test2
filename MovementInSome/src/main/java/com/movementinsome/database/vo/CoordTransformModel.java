package com.movementinsome.database.vo;


public class CoordTransformModel{

    /**
     * 
     */
    private static final long serialVersionUID = 4002440912898388382L;

    private Long itiId;

    private String itiCoordinatesystemname;

    private String itiCoordinate;

    private Double itiSDx;

    private Double itiSDy;

    private Double itiSDz;

    private Double itiSQx;

    private Double itiSQy;

    private Double itiSQz;

    private Double itiSScale;

    private Double itiFDx;

    private Double itiFDy;

    private Double itiFScale;

    private Double itiFRotateangle;

    private Long itiPProjectiontype;

    private Double itiPCentralmeridian;

    private Double itiPScale;

    private Double itiPConstantx;

    private Double itiPConstanty;

    private Double itiPBenchmarklatitude;

    private Double itiSemimajor;

    private Double itiFlattening;

    public CoordTransformModel() {

    }

    public CoordTransformModel(Long itiId, Double itiSDx, Double itiSDy, Double itiSDz,
            Double itiSQx, Double itiSQy, Double itiSQz, Double itiSScale, Double itiFDx,
            Double itiFDy, Double itiFScale, Double itiFRotateangle, Long itiPProjectiontype,
            Double itiPCentralmeridian, Double itiPScale, Double itiPConstantx,
            Double itiPConstanty, Double itiPBenchmarklatitude, Double itiSemimajor,
            Double itiFlattening) {
        this.itiId = itiId;
        this.itiSDx = itiSDx;
        this.itiSDy = itiSDy;
        this.itiSDz = itiSDz;
        this.itiSQx = itiSQx;
        this.itiSQy = itiSQy;
        this.itiSQz = itiSQz;
        this.itiSScale = itiSScale;
        this.itiFDx = itiFDx;
        this.itiFDy = itiFDy;
        this.itiFScale = itiFScale;
        this.itiFRotateangle = itiFRotateangle;
        this.itiPProjectiontype = itiPProjectiontype;
        this.itiPCentralmeridian = itiPCentralmeridian;
        this.itiPScale = itiPScale;
        this.itiPConstantx = itiPConstantx;
        this.itiPConstanty = itiPConstanty;
        this.itiPBenchmarklatitude = itiPBenchmarklatitude;
        this.itiSemimajor = itiSemimajor;
        this.itiFlattening = itiFlattening;
    }

    public Long getItiId() {
        return itiId;
    }

    public void setItiId(Long itiId) {
        this.itiId = itiId;
    }

    public String getItiCoordinatesystemname() {
        return itiCoordinatesystemname;
    }

    public void setItiCoordinatesystemname(String itiCoordinatesystemname) {
        this.itiCoordinatesystemname = itiCoordinatesystemname;
    }

    public Double getItiSDx() {
        return itiSDx;
    }

    public void setItiSDx(Double itiSDx) {
        this.itiSDx = itiSDx;
    }

    public Double getItiSDy() {
        return itiSDy;
    }

    public void setItiSDy(Double itiSDy) {
        this.itiSDy = itiSDy;
    }

    public Double getItiSDz() {
        return itiSDz;
    }

    public void setItiSDz(Double itiSDz) {
        this.itiSDz = itiSDz;
    }

    public Double getItiSQx() {
        return itiSQx;
    }

    public void setItiSQx(Double itiSQx) {
        this.itiSQx = itiSQx;
    }

    public Double getItiSQy() {
        return itiSQy;
    }

    public void setItiSQy(Double itiSQy) {
        this.itiSQy = itiSQy;
    }

    public Double getItiSQz() {
        return itiSQz;
    }

    public void setItiSQz(Double itiSQz) {
        this.itiSQz = itiSQz;
    }

    public Double getItiSScale() {
        return itiSScale;
    }

    public void setItiSScale(Double itiSScale) {
        this.itiSScale = itiSScale;
    }

    public Double getItiFDx() {
        return itiFDx;
    }

    public void setItiFDx(Double itiFDx) {
        this.itiFDx = itiFDx;
    }

    public Double getItiFDy() {
        return itiFDy;
    }

    public void setItiFDy(Double itiFDy) {
        this.itiFDy = itiFDy;
    }

    public Double getItiFScale() {
        return itiFScale;
    }

    public void setItiFScale(Double itiFScale) {
        this.itiFScale = itiFScale;
    }

    public Double getItiFRotateangle() {
        return itiFRotateangle;
    }

    public void setItiFRotateangle(Double itiFRotateangle) {
        this.itiFRotateangle = itiFRotateangle;
    }

    public Long getItiPProjectiontype() {
        return itiPProjectiontype;
    }

    public void setItiPProjectiontype(Long itiPProjectiontype) {
        this.itiPProjectiontype = itiPProjectiontype;
    }

    public Double getItiPCentralmeridian() {
        return itiPCentralmeridian;
    }

    public void setItiPCentralmeridian(Double itiPCentralmeridian) {
        this.itiPCentralmeridian = itiPCentralmeridian;
    }

    public Double getItiPScale() {
        return itiPScale;
    }

    public void setItiPScale(Double itiPScale) {
        this.itiPScale = itiPScale;
    }

    public Double getItiPConstantx() {
        return itiPConstantx;
    }

    public void setItiPConstantx(Double itiPConstantx) {
        this.itiPConstantx = itiPConstantx;
    }

    public Double getItiPConstanty() {
        return itiPConstanty;
    }

    public void setItiPConstanty(Double itiPConstanty) {
        this.itiPConstanty = itiPConstanty;
    }

    public Double getItiPBenchmarklatitude() {
        return itiPBenchmarklatitude;
    }

    public void setItiPBenchmarklatitude(Double itiPBenchmarklatitude) {
        this.itiPBenchmarklatitude = itiPBenchmarklatitude;
    }

    public Double getItiSemimajor() {
        return itiSemimajor;
    }

    public void setItiSemimajor(Double itiSemimajor) {
        this.itiSemimajor = itiSemimajor;
    }

    public Double getItiFlattening() {
        return itiFlattening;
    }

    public void setItiFlattening(Double itiFlattening) {
        this.itiFlattening = itiFlattening;
    }

    public String getItiCoordinate() {
        return itiCoordinate;
    }

    public void setItiCoordinate(String itiCoordinate) {
        this.itiCoordinate = itiCoordinate;
    }
}
