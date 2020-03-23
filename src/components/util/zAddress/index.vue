<template>
	<view class="z-address" v-if="showPopup" @touchmove.stop.prevent="clear">
		<view
			class="z-address-mask"
			@touchmove.stop.prevent="clear"
			v-if="maskClick"
			:class="[ani+'-mask', animation ? 'mask-ani' : '']"
			:style="{
					'background-color': maskBgColor
				}"
			@tap="hideMask(true)"
		></view>
		<view
			class="z-address-content z-address-fixed"
			:class="[type,ani+'-content', animation ? 'content-ani' : '']"
		>
			<view class="z-address_header">
				<view class="z-address_header-btn-box" @click="pickerCancel">
					<text class="z-address_header-text">取消</text>
				</view>
				<view class="z-address_header-btn-box" @click="pickerConfirm">
					<text class="z-address_header-text" :style="{color:themeColor}">确定</text>
				</view>
			</view>
			<view class="z-address_box">
				<picker-view
					indicator-style="height: 70rpx;"
					class="z-address-view"
					:value="pickerValue"
					@change="pickerChange"
				>
					<picker-view-column>
						<!-- #ifndef APP-NVUE -->
						<view class="picker-item" v-for="(item,index) in provinceDataList" :key="index">{{item.label}}</view>
						<!-- #endif -->
						<!-- #ifdef APP-NVUE -->
						<text class="picker-item" v-for="(item,index) in provinceDataList" :key="index">{{item.label}}</text>
						<!-- #endif -->
					</picker-view-column>
					<picker-view-column>
						<!-- #ifndef APP-NVUE -->
						<view class="picker-item" v-for="(item,index) in cityDataList" :key="index">{{item.label}}</view>
						<!-- #endif -->
						<!-- #ifdef APP-NVUE -->
						<text class="picker-item" v-for="(item,index) in cityDataList" :key="index">{{item.label}}</text>
						<!-- #endif -->
					</picker-view-column>
					<picker-view-column>
						<!-- #ifndef APP-NVUE -->
						<view class="picker-item" v-for="(item,index) in areaDataList" :key="index">{{item.label}}</view>
						<!-- #endif -->
						<!-- #ifdef APP-NVUE -->
						<text class="picker-item" v-for="(item,index) in areaDataList" :key="index">{{item.label}}</text>
						<!-- #endif -->
					</picker-view-column>
				</picker-view>
			</view>
		</view>
	</view>
</template>

<script>
import provinceData from './province.js';
import cityData from './city.js';
import areaData from './area.js';
export default {
	name: 'zAddress',
	props: {
		animation: {
			type: Boolean,
			default: true
		},
		type: {
			type: String,
			default: 'bottom'
		},
		// maskClick
		maskClick: {
			type: Boolean,
			default: true
		},
		show: {
			type: Boolean,
			default: true
		},
		maskBgColor: {
			type: String,
			default: 'rgba(0, 0, 0, 0.4)'
		},
		themeColor: {
			type: String,
			default: '' // 主题色
		},
		pickerValueDefault: {
			type: Array,
			default() {
				return [0, 0, 0];
			}
		}
	},
	data() {
		return {
			ani: '',
			showPopup: false,
			pickerValue: [0, 0, 0],
			provinceDataList: [],
			cityDataList: [],
			areaDataList: []
		};
	},
	watch: {
		show(newValue) {
			if (newValue) {
				this.open();
			} else {
				this.close();
			}
		},
		pickerValueDefault() {
			this.init();
		}
	},
	created() {
		this.init();
	},
	methods: {
		init() {
			this.handPickValueDefault();
			this.provinceDataList = provinceData;
			this.cityDataList = cityData[this.pickerValueDefault[0]];
			this.areaDataList =
				areaData[this.pickerValueDefault[0]][this.pickerValueDefault[1]];
			this.pickerValue = this.pickerValueDefault;
		},
		handPickValueDefault() {
			if (this.pickerValueDefault !== [0, 0, 0]) {
				if (this.pickerValueDefault[0] > provinceData.length - 1) {
					this.pickerValueDefault[0] = provinceData.length - 1;
				}
				if (
					this.pickerValueDefault[1] >
					cityData[this.pickerValueDefault[0]].length - 1
				) {
					this.pickerValueDefault[1] =
						cityData[this.pickerValueDefault[0]].length - 1;
				}
				if (
					this.pickerValueDefault[2] >
					areaData[this.pickerValueDefault[0]][this.pickerValueDefault[1]]
						.length -
						1
				) {
					this.pickerValueDefault[2] =
						areaData[this.pickerValueDefault[0]][this.pickerValueDefault[1]]
							.length - 1;
				}
			}
		},
		pickerChange(e) {
			let changePickerValue = e.detail.value;
			if (this.pickerValue[0] !== changePickerValue[0]) {
				this.cityDataList = cityData[changePickerValue[0]];
				this.areaDataList = areaData[changePickerValue[0]][0];
				changePickerValue[1] = 0;
				changePickerValue[2] = 0;
			} else if (this.pickerValue[1] !== changePickerValue[1]) {
				this.areaDataList =
					areaData[changePickerValue[0]][changePickerValue[1]];
				changePickerValue[2] = 0;
			}
			this.pickerValue = changePickerValue;
			this._$emit('onChange');
		},
		_$emit(emitName) {
			let pickObj = {
				label: this._getLabel(),
				value: this.pickerValue,
				cityCode: this._getCityCode(),
				areaCode: this._getAreaCode(),
				provinceCode: this._getProvinceCode()
			};
			this.$emit(emitName, pickObj);
		},
		_getLabel() {
			let pcikerLabel =
				this.provinceDataList[this.pickerValue[0]].label +
				'-' +
				this.cityDataList[this.pickerValue[1]].label +
				'-' +
				this.areaDataList[this.pickerValue[2]].label;
			return pcikerLabel;
		},
		_getCityCode() {
			return this.cityDataList[this.pickerValue[1]].value;
		},
		_getProvinceCode() {
			return this.provinceDataList[this.pickerValue[0]].value;
		},
		_getAreaCode() {
			return this.areaDataList[this.pickerValue[2]].value;
		},
		clear() {},
		hideMask() {
			this._$emit('onCancel');
			this.close();
		},
		pickerCancel() {
			this._$emit('onCancel');
			this.close();
		},
		pickerConfirm() {
			this._$emit('onConfirm');
			this.close();
		},
		open() {
			this.showPopup = true;
			this.$nextTick(() => {
				setTimeout(() => {
					this.ani = 'simple-' + this.type;
				}, 100);
			});
		},
		close(type) {
			if (!this.maskClick && type) return;
			this.ani = '';
			this.$nextTick(() => {
				setTimeout(() => {
					this.showPopup = false;
				}, 300);
			});
		}
	}
};
</script>

<style lang="less" scoped>
@import 'index.less';
</style>
