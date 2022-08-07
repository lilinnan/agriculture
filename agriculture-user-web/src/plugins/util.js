import Vue from 'vue';


Vue.prototype.$search = function getDataFromApi(url, option, key, other) {
  let sort = "";
  let sortBy = option.sortBy;
  let sortDesc = option.sortDesc;
  for (let i = 0; i < sortBy.length; i++) {
    sort += sortDesc[i] ? "+" : "-";
    sort += sortBy[i];
  }
  let params = {
    pageNum: option.page,
    pageSize: option.itemsPerPage,
  };
  if (sort !== "") {
    Object.assign(params, {sort: sort});
  }
  if (key !== undefined) {
    Object.assign(params, {key: key});
  }
  if (other !== undefined) {
    Object.assign(params, other);
  }
  return this.$http.get(url, {
    params: params
  })
}

Vue.prototype.$toDecimal2 = function (x) {
  x /= 100
  x = x + '';
  if (x.indexOf('.') === -1) {
    return x + '.00';
  }
  let n = 3 - (x.length - x.indexOf('.'));
  for (let i = 0; i < n; i++) {
    x += '0';
  }
  return x;
}

Vue.prototype.$isPc = function () {
  let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i);
  return !flag;
}