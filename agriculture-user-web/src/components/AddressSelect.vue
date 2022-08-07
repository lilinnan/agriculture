<template>
  <v-container>
    <v-subheader>
      您当前选择的地址是：{{ address }}
    </v-subheader>

    <v-tabs v-model="tab" background-color="primary" dark show-arrows>
      <v-tab href="#k0">
        {{ data[0] && data[0].name ? data[0].name : '请选择省/市' }}
      </v-tab>
      <v-tab href="#k1" v-if="showCities.length>0">
        {{ data[1] && data[1].name ? data[1].name : '请选择市' }}
      </v-tab>
      <v-tab href="#k2" v-if="showAreas.length>0">
        {{ data[2] && data[2].name ? data[2].name : '请选择县/区' }}
      </v-tab>
      <v-tab href="#k3" v-if="showTowns.length>0">
        {{ data[3] && data[3].name ? data[3].name : '请选择乡/街道' }}
      </v-tab>
    </v-tabs>

    <v-tabs-items v-model="tab">
      <v-tab-item value="k0">
        <v-list>
          <v-list-item v-for="p of provinces" @click="setProvince(p)">
            {{ p.name }}
          </v-list-item>
        </v-list>
      </v-tab-item>
      <v-tab-item value="k1" v-if="showCities.length>0">
        <v-list>
          <v-list-item v-for="c of showCities" @click="setCity(c)">
            {{ c.name }}
          </v-list-item>
        </v-list>
      </v-tab-item>
      <v-tab-item value="k2" v-if="showAreas.length>0">
        <v-list>
          <v-list-item v-for="a of showAreas" @click="setArea(a)">
            {{ a.name }}
          </v-list-item>
        </v-list>
      </v-tab-item>
      <v-tab-item value="k3" v-if="showTowns.length>0">
        <v-list>
          <v-list-item v-for="t of showTowns" @click="setTown(t)">
            {{ t.name }}
          </v-list-item>
        </v-list>
      </v-tab-item>
    </v-tabs-items>
  </v-container>
</template>

<script>
export default {
  name: "AddressSelect",
  props: {
    value: String
  },
  data: () => ({
    tab: "k0",
    provinces: [],
    cities: [],
    areas: [],
    towns: [],
    showCities: [],
    showAreas: [],
    showTowns: [],
    data: [],
    address: ''
  }),
  methods: {
    setProvince(p) {
      this.data[0] = p;
      this.data[1] = undefined;
      this.data[2] = undefined;
      this.data[3] = undefined;
      this.showCities = [];
      this.showAreas = [];
      this.showTowns = [];
      for (let city of this.cities) {
        if (city.province === p.province) {
          this.showCities.push(city);
        }
      }
      this.updateAddress();
      if (this.showCities.length === 0) {
        this.setCity(p);
        return;
      }
      this.tab = 'k1';
    },
    setCity(c) {
      this.data[1] = c;
      this.data[2] = undefined;
      this.data[3] = undefined;
      this.showAreas = [];
      this.showTowns = [];
      for (let area of this.areas) {
        if ((c.city === undefined || area.city === c.city) && area.province === c.province) {
          this.showAreas.push(area);
        }
      }
      this.updateAddress();
      if (this.showAreas.length === 0) {
        this.setArea(c);
        return;
      }
      setTimeout(() => {
        this.tab = 'k2';
      }, 0);
    },
    setArea(a) {
      this.data[2] = a;
      this.data[3] = undefined;
      this.showTowns = [];
      for (let town of this.towns) {
        if ((a.area && town.area === a.area) && (a.city && town.city === a.city) && town.province === a.province) {
          this.showTowns.push(town);
        }
      }
      this.updateAddress();
      if (this.showTowns.length === 0) {
        this.setTown(a);
        return;
      }
      this.showTowns.push({name: '暂不选择', id: -1})
      this.tab = 'k3';

    },
    setTown(t) {
      this.data[3] = t;
      this.updateAddress();
      this.$emit("input", this.address);
    },
    clear() {
      this.data[0] = undefined;
      this.data[1] = undefined;
      this.data[2] = undefined;
      this.data[3] = undefined;
      this.showCities = [];
      this.showAreas = [];
      this.showTowns = [];
      this.tab = 'k0';
    },
    updateAddress() {
      let address = '';
      if (this.data[0]) {
        address += this.data[0].name;
      }
      if (this.showCities.length > 0 && this.data[1]) {
        address += this.data[1].name;
      }
      if (this.showAreas.length > 0 && this.data[2]) {
        address += this.data[2].name;
      }
      if (this.showTowns.length > 0 && this.data[3] && this.data[3].id !== -1) {
        address += this.data[3].name;
      }
      this.address = address;
    }
  },
  mounted() {
    const {province, city, area, town} = require('province-city-china/data');
    this.provinces = province;
    this.cities = city;
    this.cities.push({
      "code": "469000",
      "name": "省直辖县级行政区划",
      "province": "46",
      "city": "90",
      "area": 0,
      "town": 0
    })
    this.areas = area;
    this.towns = town;
  },
}
</script>

<style scoped>

</style>