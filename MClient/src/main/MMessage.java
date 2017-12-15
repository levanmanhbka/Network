package main;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class MMessage {
	public static final String TEST = "test";
	public static final String SIGN_UP = "sign_up";
	public static final String LOG_IN = "log_in";
	public static final String LOG_OUT = "log_out";
	public static final String MESSAGE = "message";
	public static final String USERS_LIST = "users_list";
	public static final String STOP_SERVICE = "stop_service";
	
	public String type, sender, content, recipient;

	public MMessage() {
		type = "";
		sender = "";
		content = "";
		recipient = "";
	}

	public MMessage(String json) {
		Object obj = JSONValue.parse(json);
		JSONObject object = (JSONObject) obj;
		type = (String) object.get("type");
		sender = (String) object.get("sender");
		content = (String) object.get("content");
		recipient = (String) object.get("recipient");
	}

	public MMessage(String type, String sender, String content, String recipient) {
		this.type = type;
		this.sender = sender;
		this.content = content;
		this.recipient = recipient;
	}

	public String MessageToJsonString() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("type", type);
		jsonObject.put("sender", sender);
		jsonObject.put("content", content);
		jsonObject.put("recipient", recipient);
		return jsonObject.toString();
	}
}
