class Query {
  Query(
      {this.pageIndex = 1,
      this.pageSize = 10,
      this.searchKey = "",
      this.total = 0,
      this.totalPage = 0});

  int pageIndex = 1;
  int pageSize = 10;
  String searchKey = "";
  int total = 0;
  int totalPage = 0;

  Map<String, dynamic> toJson() => {
        'pageIndex': pageIndex,
        'pageSize': pageSize,
        'searchKey': searchKey,
      };
}
