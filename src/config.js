/*
 * @Author: seekwe
 * @Date: 2019-12-27 15:47:48
 * @Last Modified by:: seekwe
 * @Last Modified time: 2020-06-11 12:04:48
 */

let apiHost, websiteUrl;

// 售完的盒子数量
export const soldOut = 2;

// 抽盒超时时间
export const boxExtractionTimeout = 600*3;

export const posterCopywriting = '看我在王牌化身抽盒机化身成了哪位巨星';
export const posterCopywritingAll = '来看看我的王牌阵容都有谁';

export const music = 'https://blindbox.fancier.store/images/Dribble2much.mp3';

let [appName, apiTimeout, autoTimeout] = ['王牌化身福盒机', 20000, 60]; //小程序默认名称, 接口超时时间ms, 重新授权获取用户信息间隔时间ms(0不会过期)

if (process.env.NODE_ENV !== 'development') {
  apiHost = 'https://blindbox.fancier.store'; // 接口域名
  websiteUrl = 'https://blindbox.fancier.store'; // 静态资源域名
} else {
  apiHost = 'https://blindbox.fancier.store'; // 测试接口域名
  websiteUrl = 'https://blindbox.fancier.store'; // 测试静态资源域名
}

// 微信订阅活动模块 ID
export const ActivityTmplids = ['EtU35HZ2aNqUGpLdHl5PBc9VV1P9tqD-bgCIIIXGjes'];

// 购买盒子页面底部提示文案
export const buyBootomTips = '';

// 卡券说明文案 content 支持 html
export const CardDescription = {
         tips: {
           title: '提示卡说明',
           type: 'tip',
           content:
             '<div style="text-align: center;">使用本卡可以获得一次排出提示。<br><br>（即福盒内不会抽到上方划掉的款式）</div>',
         },
         display: {
           type: 'display',
           title: '显示卡说明',
           content:
             '使用本卡可以直接显示福盒内的款式。',
         },
         discount: {
           type: 'discount',
           title: '优惠卡说明',
           content:
             '使用本卡可以在付款结算时以直接抵扣的方式获得优惠。',
         },
       };

// 我的页面规则文案 content 支持 html
export const RuleDescription = {
  agreement: {
    title: '王牌化身福盒机服务协议',
    content: `
<br>
<br>提示条款
<br>欢迎您与各王牌化身福盒机经营者(详见定义条款)共同签署本《王牌化身福盒机服务协议》(下称“本协议”)并使用王牌化身福盒机服务!
<br>各服务条款前所列索引关键词仅为帮助您理解该条款表达的主旨之用,不影响或限制本协议条款的含义或解释。为维护您自身权益,建议您仔细阅读各条款具体表述。
<br>【审慎阅读】您在申请注册流程中点击同意本协议之前,应当认真阅读本协议。请您务必审慎阅读、充分理解各条款内容,特别是免除或者限制责任的条款、法律适用和争议解决条款。免除或者限制责任的条款将以粗体下划线标识,您应重点阅读。阅读本协议的过程中,如果您不同意本协议或其中任何条款约定,您应立即停止注册程序。如您对协议有任何疑问,可向王牌化身福盒机客服咨询。
<br>【签约动作】当您按照注册页面提示填写信息、阅读并同意本协议且完成全部注册程序后,即表示您已充分阅读、理解并接受本协议的全部内容,并与王牌化身福盒机达成一致,成为王牌化身福盒机“用户”。
<br>
<br>一、定义
<br>王牌化身福盒机:指王牌化身福盒机微信小程序(名称为王牌化身福盒机)。
<br>王牌化身福盒机经营者:北京妙趣盒生文化传播有限公司。
<br>王牌化身福盒机服务:王牌化身福盒机基于互联网,以包含王牌化身福盒机微信小程序等在内的各种形态(包括未来技术发展出现的新的服务形态)向您提供的各项服务。
<br>王牌化身福盒机规则:包括在所有王牌化身福盒机规则频道内已经发布及后续发布的全部规则、解读、公告等内容以及各在网站、圈子、论坛、帮助中心内发布的各类则、实施细则、产品流程说明、公告等。
<br>同一用户:使用同一身份认证信息或经王牌化身福盒机排查认定多个王牌化身福盒机账户的实际控制人为同一人的,均视为同一用户。
<br> 	
<br>二、协议范围
<br>2.1签约主体
<br>【平等主体】本协议由您与王牌化身福盒机经营者共同缔结,本协议对您与王牌化身福盒机经营者均具有合同效力。
<br>【主体信息】王牌化身福盒机经营者是指经营王牌化身福盒机的各法律主体。本协议项下,王牌化身福盒机经营者可能根据王牌化身福盒机的业务调整而发生变更,变更后的王牌化身福盒机经营者与您共同履行本协议并向您提供服务,王牌化身福盒机经营者的变更不会影响您本协议项下的权益。王牌化身福盒机经营者还有可能因为提供新的王牌化身福盒机服务而新增,如您使用新增的王牌化身福盒机服务的,视为您同意新增的王牌化身福盒机经营者与您共同履行本协议。发生争议时,您可根据您具体使用的服务及对您权益产生影响的具体行为对象确定与您履约的主体及争议相对方。
<br>2.2补充协议
<br>由于互联网高速发展,您与王牌化身福盒机签署的本协议列明的条款并不能完整罗列并覆盖您与王牌化身福盒机所有权利与义务,现有的约定也不能保证完全符合未来发展的需求。因此,王牌化身福盒机未来新增的协议等均为本协议的补充协议,与本协议不可分割且具有同等法律效力。如有新增协议,王牌化身福盒机将对您进行告知,如您使用王牌化身福盒机服务,视为您同意上述补充协议。
<br>
<br>三、账户注册与使用
<br>3.1用户资格
<br>您确认,在您开始注册程序使用王牌化身福盒机服务前,您应当具备中华人民共和国法律规定的与您行为相适应的民事行为能力。若您不具备前述与您行为相适应的民事行为能力,则您及您的监护人应依照法律规定承担因此而导致的一切后果。
<br>此外,您还需确保您不是任何国家、国际组织或者地域实施的贸易限制、制裁或其他法律、规则限制的对象,否则您可能无法正常注册及使用王牌化身福盒机服务。
<br>3.2账户说明
<br>【账户获得】目前王牌化身福盒机使用微信提供的第三方平台账户登陆,您根据登陆时的相关提示填写必要信息后，您将获得王牌化身福盒机账户。
<br> 王牌化身福盒机只允许每位用户使用一个王牌化身福盒机账户。如又证据证明或王牌化身福盒机根据王牌化身福盒机规则判断您存在不当注册或不当使用多个王牌化身福盒机账户的情形，王牌化身福盒机可采取冻结或关闭账户、取消订单、拒绝提供服务等措施，如给王牌化身福盒机及相关方造成损失的，您还应承担赔偿责任。
<br>【账户使用】由于您的王牌化身福盒机账户关联您的个人信息及王牌化身福盒机商业信息,您的王牌化身福盒机账户仅限您本人使用。未经王牌化身福盒机同意,您直接或间接授权第三方使用您王牌化身福盒机账户或获取您账户项下信息的行为无效。如王牌化身福盒机根据王牌化身福盒机规则中约定的违约认定程序及标准判断您王牌化身福盒机账户的使用可能危及您的账户安全及/或王牌化身福盒机信息安全的,王牌化身福盒机可拒绝提供相应服务或终止本协议。
<br>【账户转让】由于用户账户关联用户信用信息,仅当有法律明文规定、司法裁定或经王牌化身福盒机同意,并符合王牌化身福盒机规则规定的用户账户转让流程的情况下,您可进行账户的转让。您的账户一经转让,该账户项下权利义务一并转移。除此外,您的账户不得以任何方式转让,否则王牌化身福盒机有权追究您的违约责任,且由此产生的一切责任均由您承担。
<br>【实名认证】作为王牌化身福盒机经营者,为使您更好地使用王牌化身福盒机的各项服务,保障您的账户安全,王牌化身福盒机有权要求您按照我国法律规定完成实名认证。
<br>
<br>3.3注册信息管理
<br>3.3.1真实合法
<br>【信息真实】在使用王牌化身福盒机服务时,您应当按王牌化身福盒机页面的提示准确完整地提供您的信息(包括您的姓名及电子邮件地址、联系电话、联系地址等),以便王牌化身福盒机或其他用户与您联系。您了解并同意,您有义务保持您提供信息的真实性及有效性。
<br>3.3.2更新维护
<br>您应当及时更新您提供的信息,在法律有明确规定要求王牌化身福盒机作为服务提供者必须对部分用户(如卖家等)的信息进行核实的情况下,王牌化身福盒机将依法不时地对您的信息进行检查核实,您应当配合提供最新、真实、完整、有效的信息。
<br>如王牌化身福盒机按您最后一次提供的信息与您联系未果、您未按王牌化身福盒机的要求及时提供信息、您提供的信息存在明显不实或行政司法机关核实您提供的信息无效的,您将承担因此对您自身、他人及王牌化身福盒机造成的全部损失与不利后果。王牌化身福盒机可向您发出询问或要求整改的通知,并要求您进行重新认证,直至中止、终止对您提供部分或全部王牌化身福盒机服务,王牌化身福盒机对此不承担责任。
<br>
<br>3.4账户安全规范
<br>【账户安全保管义务】由于王牌化身福盒机目前采用微信平台账户登陆,您的账户为您自行设置并由您保管,建议您务必保管好您的账户,账户因您主动泄露或因您遭受他人攻击、诈骗等行为导致的损失及后果,王牌化身福盒机并不承担责任,您应通过司法、行政等救济途径向侵权行为人追偿。
<br>【账户行为责任自负】除王牌化身福盒机存在过错外,您应对您账户项下的所有行为结果(包括但不限于在线签署各类协议、发布信息、购买商品及服务及披露信息等)负责。
<br>【日常维护须知】如发现任何未经授权使用您账户登录王牌化身福盒机或其他可能导致您账户遭窃、遗失的情况,建议您立即通知王牌化身福盒机。您理解王牌化身福盒机对您的任何请求采取行动均需要合理时间,且王牌化身福盒机应您请求而采取的行动可能无法避免或阻止侵害后果的形成或扩大,除王牌化身福盒机存在法定过错外,王牌化身福盒机不承担责任。
<br>
<br>
<br>四、王牌化身福盒机服务及规范
<br>【服务概况】您有权在王牌化身福盒机上享受商品购买、积分获取/消费等服务。王牌化身福盒机提供的服务内容众多,具体您可登录王牌化身福盒机浏览。
<br>4.1商品及/或服务的购买
<br>【商品及/或服务的购买】当您在王牌化身福盒机购买商品及/或服务时,请您务必仔细确认所购商品的品名、价格、数量、型号、规格、尺寸或服务的时间、内容、限制性要求等重要事项,并在下单时核实您的联系地址、电话、收货人等信息。
<br>如您填写的收货人非您本人,则该收货人的行为和意思表示产生的法律后果均由您承担。
<br>您的购买行为应当基于真实的消费需求,不得存在对商品及/或服务实施恶意购买、恶意维权等扰乱王牌化身福盒机正常交易秩序的行为。基于维护王牌化身福盒机交易秩序及交易安全的需要,王牌化身福盒机发现上述情形时可主动执行关闭相关交易订单等操作。
<br>福盒类产品具有随机性的特点，请确保您已充分了解。您应该对您的消费行为的性质和后果自行加以判断，并对您账户下的一切消费行为负责。基于产品的特点，我们无法保证您购买的商品符合您对某一特定款式或型号的预期。对此，您不应以未收到特定款式的商品要求王牌化身福盒机退款、赔偿或承担任何责任。王牌化身福盒机无法且不会对因前述问题而导致的任何损失或损害承担责任。 
<br>
<br>4.2交易争议处理
<br>【交易争议处理途径】您在王牌化身福盒机交易过程中发生争议的,您有权选择以下途径解决:
<br>(一)与争议相对方自主协商;
<br>(二)请求消费者协会或者其他依法成立的调解组织调解;
<br>(三)向有关行政部门投诉;
<br>(四)根据与争议相对方达成的仲裁协议(如有)提请仲裁机构仲裁;
<br>(五)向人民法院提起诉讼。
<br>
<br>4.3费用
<br>王牌化身福盒机为王牌化身福盒机向您提供的服务付出了大量的成本,除王牌化身福盒机明示的收费业务外,王牌化身福盒机向您提供的服务目前是免费的。如未来王牌化身福盒机向您收取合理费用,王牌化身福盒机会采取合理途径并以足够合理的期限提前通过法定程序并以本协议第八条约定的方式通知您，确保您有充分选择的权利。
<br>
<br>4. 4王牌化身福盒机规则
<br>由于王牌化身福盒机提供的服务众多，不同的服务其表现形式，操作流程与规范可能存在巨大差异，针对不同类型的服务，王牌化身福盒机将提供不同的规则用于说明、解释服务的使用方法与规范，您需同意不同类型服务所对应的规则才可以使用该服务，如您不同意该规则，则视为放弃该服务所属所有功能与其他服务中含有的与该服务关联的功能，您可能因此无法使用其他服务的部分或全部功能。如您使用该服务，则视为您同意该服务的所有规则。
<br>
<br>4. 5责任限制
<br>【不可抗力及第三方原因】王牌化身福盒机依照法律规定履行基础保障义务，但对于下述原因导致的合同履行障碍、履行瑕疵、 履行延后或履行内容变更等情形，王牌化身福盒机并不承担相应的违约责任：
<br>（一）因自然灾害、罢工、暴乱、战争、政府行为、司法行政命令等不可抗力因素；
<br>（二）因电力供应故障、通讯网络故障等公共服务因素或第三人因素；
<br>（三）在王牌化身福盒机已尽善意管理的情况下，因常规或紧急的设备与系统维护、设备与系统故障、网络信息与数据安全等因素。
<br>
<br>五、用户信息的保护及授权
<br>5. 1个人信息的保护
<br>王牌化身福盒机非常重视用户个人信息（即能够独立或与其他信息结合后识别用户身份的信息） 的保护，在您使用王牌化身福盒机提供的服务时，您同意、王牌化身福盒机按照在王牌化身福盒机上公布的隐私权政策在符合微信平台相关协议的基础上收集、存储、使用、披露和保护您的个人信息。
<br>5. 2非个人信息的保证与授权
<br>【信息的发布】您声明并保证，您对您所发布的信息拥有相应、 合法的权利。否则，王牌化身福盒机可对您发布的信息依法或依本协议进行删除或屏蔽。
<br>【禁止性信息】 您应当确保您所发布的信息不包含以下内容：
<br>（一）违反国家法律法规禁止性规定的；
<br>（二）政治宣传、封建迷信、淫秽、色情、赌博、暴力、恐怖或者教唆犯罪的；
<br>（三）欺诈、虚假、不准确或存在误导性的；（四）侵犯他人知识产权或涉及第三方商业秘密及其他专有权利的；
<br>（五）侮辱、诽谤、恐吓、涉及他人隐私等侵害他人合法权益的；
<br>（六）存在可能破坏、篡改、删除、影响王牌化身福盒机任何系统正常运行或未经授权秘密获取王牌化身福盒机及其他用户的数据、个人资料的病毒、木马、爬虫等恶意软件、程序代码的；
<br>（七）其他违背社会公共利益或公共道德或依据相关王牌化身福盒机协议、规则的规定不适合在王牌化身福盒机上发布的。
<br>
<br>六、用户的违约及处理
<br>发生如下情形之一的，视为您违约：
<br>（一）使用王牌化身福盒机服务时违反有关法律法规规定的；
<br>（二）违反本协议或本协议补充协议（即本协议第2. 2条）约定的。
<br>为满足海量用户对高效优质服务的需求，您理解并同意，王牌化身福盒机可在王牌化身福盒机规则中约定违约认定的程序和标准。如：王牌化身福盒机可依据您的用户数据与海量用户数据的关系来认定您是否构成违约；您有义务对您的数据异常现象进行
<br>充分举证和合理解释，否则将被认定为违约。
<br>6. 2违约处理措施
<br>【信息处理】您在王牌化身福盒机上发布的信息构成违约的，王牌化身福盒机可根据相应规则立即对相应信息进行删除、屏蔽处理或对您的商品进行下架、监管。
<br> 【行为限制】您在王牌化身福盒机上实施的行为，或虽未在王牌化身福盒机上实施但对王牌化身福盒机及其用户产生影响的行为构成违约的，王牌化身福盒机可依据相应规则对您执行账户扣分、 限制功能使用权限、中止向您提供部分或全部服务等处理措施。如您的行为构成根本违约的，王牌化身福盒机可查封您的账户，终止向您提供服务。
<br>【王牌化身福盒机支付账户处理】当您违约的同时存在欺诈、售假、盗用他人账户等特定情形或您存在危及他人交易安全或账户安全风险时，王牌化身福盒机会依照您行为的风险程度对您的王牌化身福盒机支付账户采取取消收款、资金止付等强制措施。
<br>【处理结果公示】王牌化身福盒机可将对您上述违约行为处理措施信息以及其他经国家行政或司法机关生效法律文书确认的违法信息在王牌化身福盒机上予以公示。
<br>
<br>
<br>6. 3赔偿责任
<br>如您的行为使王牌化身福盒机及／或其关联公司遭受损失（包括自身的直接经济损失、商誉损失及对外支付的赔偿金、和解款、律师费、诉讼费等间接经济损失）， 您应赔偿王牌化身福盒机及／或其关联公司的上述全部损失。
<br>如您的行为使王牌化身福盒机及／或其关联公司遭受第三人主张权利，王牌化身福盒机及／或其关联公司可在对第三人承担金钱给付等义务后就全部损失向您追偿。
<br>6. 4特别约定
<br>【关联处理】如您因严重违约导致王牌化身福盒机终止本协议时，出于维护秩序及保护消费者权益的目的，王牌化身福盒机及／或其关联公司可对与您在其他协议项下的合作采取中止甚或终止协议的措施，并以本协议第八条约定的方式通知您。
<br>如王牌化身福盒机与您签署的其他协议及王牌化身福盒机及／或其关联公司与您签署的协议中明确约定了对您在本协议项下合作进行关联处理的情形，则王牌化身福盒机出于维护秩序及保护消费者权益的目的，可在收到指令时中止甚至终止协议，并以本协议第八条约定的方式通知您。
<br>
<br>
<br>七、协议的变更
<br>王牌化身福盒机可根据国家法律法规变化及维护交易秩序、保护消费者权益需要，不时修改本协议、补充协议，变更后的协议、 补充协议（下称“变更事项”）将通过法定程序并以本协议第八条约定的方式通知您。
<br>如您不同意变更事项，您有权于变更事项确定的生效日前联系王牌化身福盒机反馈意见。如反馈意见得以采纳，王牌化身福盒机将酌情调整变更事项。
<br>如您对已生效的变更事项仍不同意的，您应当于变更事项确定的生效之日起停止使用王牌化身福盒机服务，变更事项对您不产生效力；如您在变更事项生效后仍继续使用王牌化身福盒机服务，则视为您同意已生效的变更事项。
<br>
<br>八、通知
<br>8. 1有效联系方式
<br>您在注册成为王牌化身福盒机用户，并接受王牌化身福盒机服务时，您应该向王牌化身福盒机提供真实有效的联系方式（包括您的联系电话、联系地址等），对于联系方式发生变更的，您有义务及时更新有关信息，并保持可被联系的状态。
<br>您在注册王牌化身福盒机用户时生成的用于登陆王牌化身福盒机接收系统消息和即时信息的会员账号（包括子账号）， 也作为您的有效联系方式。
<br>王牌化身福盒机将向您的上述联系方式的其中之一或其中若干向您送达各类通知，而此类通知的内容可能对您的权利义务产生重大的有利或不利影
<br>响，请您务必及时关注。
<br>8. 2 通知的送达
<br>王牌化身福盒机通过上述联系方式向您发出通知，其中以电子的方式发出的书面通知，包括但不限于在王牌化身福盒机公告，向您提供的联系电话发送手机短信，向您提供的电子邮件地址发送电子邮件，向您的账号发送信息、系统消息以及站内信信息，在发送成功后即视为送达。
<br>对于在王牌化身福盒机上因交易活动引起的任何纠纷，您同意司法机关（包括但不限于人民法院）可以通过手机短信、电子邮件、即时通讯工具等现代通讯方式或邮寄方式向您送达法律文书（包括但不限于诉讼文书）。您指定接收法律文书的手机号码、电子邮箱或王牌化身福盒机账号等联系方式为您在王牌化身福盒机注册、更新时提供的手机号码、电子邮箱联系方式以及在注册用户时王牌化身福盒机生成的账号，司法机关向上述联系方式发出法律文书即视为送达。您指定的邮寄地址为您的法定联系地址或您提供的有效联系地址。
<br>您同意司法机关可采取以上一种或多种送达方式向您达法律文书，司法机关采取多种方式向您送达法律文书，送达时间以上述送达方式中最先送达的为准。
<br>您同意上述送达方式适用于各个司法程序阶段。如进入诉讼程序的，包括但不限于一审、二审、再审、执行以及督促程序等。
<br>你应当保证所提供的联系方式是准确、有效的，并进行实时更新。如果因提供的联系方式不确切，或不及时告知变更后的联系方式，使法律文书无法送达或未及时送达，由您自行承担由此可能产生的法律后果。
<br>您同意司法机关可采取以上一种或多种送达方式向您达法律文书，司法机关采取多种方式向您送达法律文书，送达时间以上述送达方式中最先送达的为准。
<br>您同意上述送达方式适用于各个司法程序阶段。如进入诉讼程序的，包括但不限于一审、二审、再审、执行以及督促程序等。
<br>你应当保证所提供的联系方式是准确、有效的，并进行实时更新。如果因提供的联系方式不确切，或不及时告知变更后的联系方式，使法律文书无法送达或未及时送达，由您自行承担由此可能产生的法律后果。
<br>
<br>九、协议的终止
<br>9. 1终止的情形
<br>【用户发起的终止】您有权通过以下任一方式终止本协议：
<br>（一）变更事项生效前您停止使用并明示不愿接受变更事项的；
<br>（二）您明示不愿继续使用王牌化身福盒机服务，且符合王牌化身福盒机终止条件的。
<br>【王牌化身福盒机发起的终止】出现以下情况时，王牌化身福盒机可以本协议第八条的所列的方式通知您终止本协议：
<br>（一）您违反本协议约定，王牌化身福盒机依据违约条款终止本协议的·
<br>（二）您盗用他人账户、发布违禁信息、骗取他人财物、售假、扰乱市场秩序、采取不正当手段谋利等行为，王牌化身福盒机依据王牌化身福盒机规则对您的账户予以查封的；
<br>（三）除上述情形外，因您多次违反王牌化身福盒机规则相关规定且情节严重，王牌化身福盒机依据王牌化身福盒机规则对您的账户予以查封的；
<br>（四）您的账户被王牌化身福盒机依据本协议回收的；（五） 您在王牌化身福盒机关联产品、泡泡玛特相关有欺诈、发布或销售假冒伪劣／侵权商品、侵犯他人合法权益或其他严重违法违约行为的；
<br>（六）其它应当终止服务的情况。
<br>
<br>9. 2 协议终止后的处理
<br>【用户信息披露】本协议终止后，除法律有明确规定外，王牌化身福盒机无义务向您或您指定的第三方披露您账户中的任何信息。
<br>【王牌化身福盒机权利】本协议终止后，王牌化身福盒机仍享有下列权利：
<br>（一）继续保存您留存于王牌化身福盒机的本协议第五条所列的各类信息；
<br>（二）对于您过往的违约行为，王牌化身福盒机仍可依据本协议向您追究违约责任。
<br>【交易处理】本协议终止后，对于您在本协议存续期间产生的交易订单，王牌化身福盒机可通知交易相对方并根据交易相对方的意愿决定是否关闭该等交易订单；如交易相对方要求继续履行的，则您应当就该等交易订单继续履行本协议及交易订单的约定，并承担因此产生的任何损失或增加的任何费用。
<br>
<br>十、 法律适用、管辖与其他
<br>【法律适用】本协议之订立、生效、解释、修订、补充、终止、执行与争议解决均适用中华人民共和国大陆地区法律；如法律无相关规定的，参照商业惯例及／或行业惯例。
<br>【管辖】您因使用王牌化身福盒机服务所产生及与王牌化身福盒机服务有关的争议，由王牌化身福盒机与您协商解决。协商不成时，任何一方均可向被告所在地有管辖权的人民法院提起诉讼。
<br>【可分性】本协议任一条款被视为废止、无效或不可执行，该条应视为可分的且并不影响本协议其余条款的有效性及可执行性。
`,
  },
  service: {
    title: '联系客服',
    content: `<p style="text-align: center;">
QQ: 378223838<br><br>
电话: 15976566645<br><br>
微信: seekwe<br><br>
<br>
<img style="max-width:100%;" src="https://wx.qlogo.cn/mmopen/vi_32/icXdI0QO7eV1m1T5dUHofNTfBShe30JXriaKTQUPLh0uz6q5xFERee9I65VCaxibIVGAm9xvsRAY4LUJL82L7fdibg/132" />
</p>`,
  },
  rule: {
    title: '王牌化身福盒机使用说明',
    content: `一、关于王牌化身福盒机(以下简称福盒机)
<br>1.福盒机包含一个或多个不同系列的玩具，每个系列中一整盒对应的初始福盒数量可能因玩具种类而有所不同。
<br>2.整盒中每个福盒都是不重复款，如内含隐藏款则随机替换其中一款。
<br>3.福盒被抽走后不会自动补充，当整盒全部被抽完时，该福盒场结束。
<br>4.购买后的福盒被送入“订单”您可前往进行发货操作。
<br>5.每个线上福盒都对应一个实体福盒，故因为数量有限，当出现库存不足一整套时，会出现排队等情况。
<br>
<br>二、排队规则
<br>1.当最后一套福盒机有玩家在玩时，后来的玩家将进入排队。
<br>2.当队伍前面的玩家抽盒结束，将依次轮到下一位玩家抽盒。
<br>3.您可以在排队时暂时离开，福盒机将自动为您保留排队状态，并在即将轮到您抽盒时提示您上线
<br>4.如果轮到您上线，但您长时间不进行游玩，您将被取消该福盒机的游玩资格，您可选择其他福盒机继续游玩。
<br>
<br>三、抽盒规则
<br>1.每个全新的福盒机初始状态为满盒。
<br>2.抽盒时，您可以选择任何一个未被拆开的福盒抽取，抽取时您需要先行购买该福盒，支付完成后才可以拆开查看内容。
<br>3.您在选择福盒后，直到您点击。就买它。确认前，您都可以更换其他福盒。
<br>4.拆盒后，该福盒将进入您的订单，您可进行申请发货等相关操作，或者留在该页面继续抽盒。
<br>5.当您离开该抽盒页面后，则未抽完的福盒由系统回收。重新加入游戏时，则为初始的满盒状态。
<br>
<br>四、时间限制
<br>1.您在开始抽盒后，每一步操作都有时间限制，超时您将被强制离开该福盒机。
<br>
<br>五、分享
<br>1.您在选择福盒后，可以将该福盒分享到微信，并获得积分值奖励。
<br>
<br>六、积分值
<br>1积分值是王牌化身福盒机里的一种数值，可以用于日常道具卡的购买和参与特殊活动，兑换奖品等多种功能。
<br>2.积分值的获取：您可通过签到、完成任务、分享福盒、分享图鉴等行为来获取积分值
<br>
<br>七、支付
<br>1.您可以使用微信支付来购买福盒。
<br>2.您一次只能选择一个福盒支付，无法一次性支付多个福盒。
<br>
<br>八、我的盒柜
<br>1.购买的商品会放入“订单”，在这里您可以查看已买到的商品列表，并进行发货等操作。
<br>2.商品在我的盒柜内分类放置，您可以看到每个商品的购买来源。
<br>
<br>九、发货
<br>1.购买后的福盒若超过保留期将自动发货至您的默认地址，您可以到"我的盒柜"手动申请发货。
<br>2.您需要填写准确的默认收货地址，我们将以该地址信息作为向您提供服务时所依凭据。
<br>3.为保证您购买的商品质量，购买的商品在盒柜内的保留时间为20天，20天内未手动发货的，系统将在保留期到达之日第二天起15天内，自动将到期商品发送到您的默认地址。自动发货的订单包含3件及以上的产品则剔红包邮服务，不足3件的产品将使用快递费到付的形式发出。
<br>4.请确保您的默认地址栏真实有效的，因您地址错误或其他个人原因导致的任何损失由您自行承担
<br>5.自动发货的订单，您因各种理由拒收造成的损失由您个人承担。
<br>6.保留时间20天说明：从您下单当日起计算，只计算自然日期，不足1天按照1天计算。预售商品从开放发货当日起计算，只计算自然日期，不足1天按照1天计算。
<br>7.15天发货说明：从保留日期截止日的第2天开始计算，15个自然天内随机日期发货。
<br>8.包邮：全场每单满3个福盒，中国大陆地区包邮，不包括偏远地区(比如新疆、西藏、内蒙古等)，港澳台及海外地区无法配送。
<br>9.发货数量不足3个的将根据地区不同收取一定运费。
<br>10.除特殊情况，所有在王牌化身福盒机小程序购买的商品都可适用于3个包邮(0元商品除外)及相关规则。
<br>
<br>十、退换货
<br>1.福盒类商品拆盒后(含在线拆盒)除重大质量问题外，不支持7天无理由退换!
<br>2.当您收到福盒时，请打开确认福盒是否存在重大质量问题。如有应在收货之日起10日内提出。否则将不予退换货。
<br>3.福盒玩具制作过程中可能会出现染色不均、轻微划痕、掉漆或起泡等出厂瑕疵问题，都属于正常现象。上述范围内不接受退换货。
<br>4.由于灯光、设备、显示器分辨率等因素，图片可能会存在些许色差，一切以实物为准。
<br>5.如遇产品外包装严重破损等情况请拒签。缺件、断裂、发错货等严重商品质量问题，请您在收到商品后的7天内明确商品问题并联系客服处理售后，7天后将不在接受任何售后处理。
<br>
<br>十一、其他问题
<br>1.如果您在使用过程中遇到问题，请在“我的”页面右上角点击联系客服按钮，根据提示联系客服咨询问题。
`,
  },
};

export default {
  websiteUrl,
  apiHost,
  appName,
  apiTimeout,
  autoTimeout,
};