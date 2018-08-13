package com.movementinsome.database.vo;



import org.codehaus.jackson.annotate.JsonIgnoreProperties;



@JsonIgnoreProperties(ignoreUnknown = true) 
public class PhotoInfoVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7213206403969746307L;
	//图片名称
	private String imageName;
	//图片大小
	private String imageSize;
	//图片类型
	private String imageType;
	//图片数据BASE64格式
	private String imageBaseCode;

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize(String imageSize) {
		this.imageSize = imageSize;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getImageBaseCode() {
		return imageBaseCode;
	}

	public void setImageBaseCode(String imageBaseCode) {
		this.imageBaseCode = imageBaseCode;
	}
	
	
}
