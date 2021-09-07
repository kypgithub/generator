package io.renren;

import io.renren.utils.HttpClientUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RenrenApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void tests() throws IOException {
		//String url = "http://uat-portal.sinoguarantee.com/sino-bfpi/api.jsp?attId=20210811190303000856&attType=pdf";
		String url = "https://fintech.sinoguarantee.com/sino-bfpi/api.jsp?attId=20210820154002000039&attType=pdf";
		Map<String, String> header = new HashMap<>();
		header.put("Content-Type", "text/plain;charset=utf-8");
		String result = HttpClientUtil.httpGet(url, header);
		byte[] bytes = Base64.getMimeDecoder().decode(result);
		InputStream inputStream = new ByteArrayInputStream(bytes);
		//生成文件地址名称
		String localFileUrl = "E:\\111f55f8-897c-44c0-bc72-69f45e699b49.pdf";
		FileOutputStream fileOutputStream = new FileOutputStream(localFileUrl);
		byte[] buffer = new byte[inputStream.available()];
		int read;
		while ((read = inputStream.read(buffer)) > 0) {
			fileOutputStream.write(buffer, 0, read);
		}
		fileOutputStream.close();
		inputStream.close();
	}
}
