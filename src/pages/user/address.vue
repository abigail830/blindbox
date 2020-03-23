<!--
 * @Author: seekwe
 * @Date: 2020-03-02 18:47:50
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-03-18 09:56:07
 -->
<template>
	<view class="page page-address">
		<view class="tip">最少保留一个收货地址</view>
		<view
			:class="{'address_on':v.isDefaultAddress}"
			class="address-item"
			v-for="(v,k) in addressList"
			:key="k"
		>
			<view @click="toggleDefault(v,k)" class="address-icon">
				<image class="address-icon-image" :src="v.isDefaultAddress?addressIcon:addressIconOff" mode />
				<view v-text="v.isDefaultAddress?'默认地址':'其他地址'"></view>
			</view>
			<view class="address-info" @click="toggleDefault(v,k)">
				<view>
					<text class="name">{{v.receiver}}</text>
					<text class="phone">{{v.mobile}}</text>
				</view>
				<view class="address one-text">{{v.detailAddress}}</view>
			</view>
			<view class="edit-btn" @click="goEdit(v)">
				<image class="edit-icon-image" :src="v.isDefaultAddress?editIcon:editIconOff" mode />
			</view>
		</view>
		<button class="add-btn" @click="goEdit()">
			<image class="add-btn-icon" :src="addBtnIcon" mode />
			<text>新建地址</text>
		</button>
	</view>
</template>

<script>
import qs from 'qs';
import editIcon from './static/edit-icon.png';
import editIconOff from './static/edit-icon-off.png';
import addressIconOff from './static/address-icon-off.png';
import addressIcon from './static/address-icon.png';
import addBtnIcon from './static/add.png';
export default {
	data() {
		return {
			editIcon: editIcon,
			editIconOff: editIconOff,
			addressIcon: addressIcon,
			addBtnIcon: addBtnIcon,
			addressIconOff: addressIconOff,
			addressList: []
		};
	},
	onShow() {
		this.goAddress();
	},
	methods: {
		toggleDefault(v, k) {
			if (v.isDefaultAddress) return;
			uni.showModal({
				content: '是否把该地址设置为默认地址',
				success: async res => {
					if (res.confirm) {
						let data = Object.assign({}, v, { isDefaultAddress: true });
						await this.$api('address.update', data);
						this.goAddress();
						// this.addressList[k].isDefault = true;
					} else if (res.cancel) {
					}
				}
			});
		},
		async goAddress() {
			this.$log('获取收货地址');
			const done = this.$loading();
			try {
				const res = await this.$api('address.get');
				this.addressList = res;
				done();
			} catch (err) {
				this.$alert('网络繁忙，请稍后再试');
				this.$log(err);
			}
		},
		goEdit(v) {
			let id = 0;
			if (!v) {
				v = { id: 0 };
			} else {
				let done = this.$loading();
				setTimeout(done, 1000);
			}
			this.$go('./address-edit?data=' + JSON.stringify(v));
		}
	}
};
</script>

<style lang='less'>
@import '../../common/var.less';
.page-address {
	padding: 55rpx;
	background-color: @lightGreyPageBackground;
	color: @fontColor;
	.tip {
		margin-bottom: 33rpx;
		font-size: 20.8rpx;
	}
}
.address-item {
	display: flex;
	font-size: 21rpx;
	align-items: center;
	justify-content: center;
	margin-bottom: 20rpx;
	background-color: @lightGreyColor;
	color: #b1b1b1;
	.radius();
	.address {
		padding-top: 20rpx;
	}
	.phone {
		float: right;
		margin-right: 30rpx;
	}
	.address-info,
	.address-icon,
	.edit-btn {
		padding: 33rpx 20rpx;
		text-align: center;
		box-sizing: border-box;
	}
	.address-info {
		flex: 1;
		width: 0;
		overflow: hidden;
		text-align: left;
	}
	.address-icon {
		width: 134rpx;
		.address-icon-image {
			width: 45rpx;
			height: 51rpx;
		}
	}
	.edit-btn {
		width: 120rpx;
		.edit-icon-image {
			width: 44rpx;
			height: 43rpx;
		}
	}
	&.address_on {
		color: @fontColor;
		background-color: @themeColor;
		.address-icon {
			background-color: #fff;
		}
	}
}
.add-btn {
	background-color: #fff;
	color: @fontColor;
	height: 158rpx;
	font-size: 24rpx;
	display: flex;
	align-items: center;
	justify-content: center;
	.add-btn-icon {
		width: 28rpx;
		height: 28rpx;
		// display: inline-block;
		// border: 2rpx solid;
		// border-radius: 100%;
		// width: 28rpx;
		// text-align: center;
		// box-sizing: border-box;
		margin-right: 10rpx;
		// transform: rotate(45deg);
		// font-weight: bold;
		// line-height: 20rpx;
		// height: 28rpx;
	}
}
</style>