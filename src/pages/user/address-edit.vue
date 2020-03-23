<!--
 * @Author: seekwe
 * @Date: 2020-03-11 10:24:31
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-18 09:58:08
 -->
<template>
	<view class="page-address-edit page">
		<view class="address-form">
			<view class="address-form-line">
				<text class="label">收货人：</text>
				<input v-model="from.receiver" class="address-form-input" type="text" placeholder="请输入联系姓名" />
			</view>
			<view class="address-form-line">
				<text class="label">联系电话：</text>
				<input class="address-form-input" v-model="from.mobile" type="tel" placeholder="请输入联系电话" />
			</view>
			<view class="address-form-line">
				<text class="label">所在地区：</text>
				<text @click="openAddres" class="address-form-input">{{from.area}}</text>
			</view>
			<view class="address-form-line">
				<text class="label">详细地址：</text>
				<textarea v-model="from.detailAddress" class="address-form-textarea" placeholder="请输入详细地址" />
			</view>
		</view>
		<checkbox-group @change="changeIsDefaultAddress">
			<label class="address-checkbox-box">
				<checkbox
					class="address-checkbox"
					checked="isDefaultAddress"
					:value="true"
					color="#FFCC33"
					style
				/>默认地址
			</label>
		</checkbox-group>
		<button v-if="showDelete" class="delete-btn" @click="deleteAddress">
			<text>删除地址</text>
		</button>

		<button class="save-btn" @click="save">
			<text>保存地址</text>
		</button>
		<zAddress :pickerValueDefault="cityPickerValueDefault" @onConfirm="onConfirm" ref="zAddress" />
	</view>
</template>

<script>
import qs from 'qs';
import { zAddress } from '@/components/util/zAddress';
let from = {
	id: 0,
	mobile: '',
	receiver: '',
	detailAddress: '',
	associateCode: '',
	isDefaultAddress: 1,
	area: '请选择所在地区'
};
if (process.env.NODE_ENV === 'development') {
	from = {
		id: 0,
		mobile: '13800138000',
		receiver: '默认',
		detailAddress: '广州',
		associateCode: [0, 0, 0],
		isDefaultAddress: 1,
		area: '北京市市辖区东城区'
	};
}
export default {
	components: {
		zAddress
	},
	onLoad(opt) {
		let data = opt.data ? JSON.parse(opt.data) : {};
		this.from = Object.assign({}, this.from, data);
		if (data.id) {
			let cityPickerValueDefault = this.from.associateCode.split('|');
			if (cityPickerValueDefault.length === 3) {
				this.cityPickerValueDefault = cityPickerValueDefault;
			}
		}
		this.$log('edit address', data);
		this.id = opt.id || 0;
	},
	data() {
		return {
			cityPickerValueDefault: [0, 0, 0],
			from: Object.assign({}, from)
		};
	},
	computed: {
		showDelete() {
			return this.from.id > 0;
		}
	},
	methods: {
		changeIsDefaultAddress() {
			this.from.isDefaultAddress = !this.from.isDefaultAddress;
		},
		async deleteAddress() {
			this.$log('删除地址');
			try {
				let res = await this.$api('address.delete', this.from.id);
				console.log(res);
			} catch (err) {
				this.$alert(err);
			}
		},
		save() {
			const data = this.from;
			this.$log(data);
			if (!data.receiver) {
				this.$alert('请输入联系人姓名');
				return;
			}
			if (!data.associateCode) {
				this.$alert('请输入选择地区');
				return;
			}
			if (!data.detailAddress) {
				this.$alert('请输入详细地址');
				return;
			}
			if (!this.$is.phone(data.mobile)) {
				this.$alert('请输入正确联系号码');
				return;
			}
			data.associateCode = data.associateCode.join('|');
			data.isDefaultAddress = !!data.isDefaultAddress;
			let api = '';
			if (this.from.id) {
				api = this.$api('address.update', data);
			} else {
				delete data['id'];
				api = this.$api('address.add', data);
			}
			api
				.then(e => {
					this.$alert('添加完成');
					this.$back();
				})
				.catch(e => {
					this.$alert(e);
				});
		},
		openAddres() {
			this.$refs.zAddress.open();
		},
		onConfirm(e) {
			this.$log(e);
			this.from.associateCode = e.value.join('|');
			this.from.area = e.label.split('-').join('');
		}
	}
};
</script>

<style lang='less'>
@import '../../common/var.less';
.address-checkbox-box {
	font-size: 28rpx;
	line-height: 60rpx;
	display: block;
	margin-top: 10rpx;
	height: 60rpx;
	// transform: scale(0.7);
	.address-checkbox {
	}
}
.save-btn,
.delete-btn {
	margin-top: 30rpx;
	background: @themeColor;
	color: #fff;
	height: 90rpx;
	font-size: 24rpx;
	display: flex;
	align-items: center;
	justify-content: center;
}
.delete-btn {
	background-color: red;
}
.page-address-edit {
	padding: @paadingSize;
	background-color: @lightGreyPageBackground;
}
.address-form-textarea,
.address-form-input {
	flex: 1;
}
.address-form {
	@radius: 10rpx;
	.radius();
	color: @fontColor;
	background-color: #fff;
}
.address-form-line {
	padding: 20rpx 30rpx;
	font-size: 28rpx;
	position: relative;
	display: flex;
	.label {
		width: 150rpx;
	}
	&:not(:last-child)::after {
		content: '';
		position: absolute;
		width: 100%;
		height: 1px;
		bottom: 0;
		left: 0;
		background-color: @lightGreyColor;
	}
}
</style>