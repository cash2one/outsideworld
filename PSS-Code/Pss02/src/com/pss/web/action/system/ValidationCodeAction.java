/**
 * ====================================================================
 * <p>版　权: Travelsky 版权所有 (c) 2006</p>
 * <p>文　件: com.ttl.dit.web.action.system.ValidationCodeAction</p>
 * <p>所含类: ValidationCodeAction</p>
 *
 * <p>修改记录:</p>
 * 日期       作者                            内容
 * =====================================================================
 * 2011-4-28 zhdwang                        建立,完成基本内容．
 * =====================================================================
 */
package com.pss.web.action.system;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.pss.common.ValidationImageUtil;
import com.pss.common.ValidationImageUtil.ImageResult;

public class ValidationCodeAction extends ActionSupport implements SessionAware{
	private Map<String,Object> session;
	private ByteArrayInputStream inputStream;
	@Override
    public String execute() throws Exception {
		ImageResult result = ValidationImageUtil.createImage(198,21);
		//获得字节的输出流
		ByteArrayInputStream input=getByteArrayIpputStream(result.getImage());
		//将验证码存入session
		saveValidationCode(result.getResult());
		//设置输入流，作为返回结果
		setInputStream(input);
        return SUCCESS;
    }
	/**
	 * 保存验证码
	 * @param validationCode
	 */
    private void saveValidationCode(String validationCode){
    	session.put("validationCode", validationCode);
    }
    private ByteArrayInputStream getByteArrayIpputStream(BufferedImage image)throws Exception{
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
        ImageOutputStream imageOut = ImageIO.createImageOutputStream(output);
        ImageIO.write(image, "JPEG", imageOut);
        imageOut.close();
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        output.close();
        return input;
    }
	@Override
	public void setSession(Map<String, Object> session) {
		// TODO Auto-generated method stub
		this.session = session;
	}
	public ByteArrayInputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(ByteArrayInputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	
}