package entity;

import java.util.Date;

public class WorkOrder {

  private Integer id;
  private String title;
  private String details;
  private Date date;
  private Integer state;
  private String comments;
  private Client client;
  private Operator operator;

  public WorkOrder() {
    state = 0;
    details = "no details";
    comments = "no comments";
  }

  public WorkOrder(Integer id) {
    this.id = id;
    state = 0;
    details = "no details";
    comments = "no comments";
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }

  public Client getClient() {
    return client;
  }

  public void setClient(Client client) {
    this.client = client;
  }

  public Operator getOperator() {
    return operator;
  }

  public void setOperator(Operator operator) {
    this.operator = operator;
  }
  
}
