package com.willis.spark

/**
 * description: none
 * @author willis.yan.ws@gmail.com
 * @date: 2023/12/27
 */
internal object Error {
    val map = HashMap<Int, String>()

    init {
        map[18007] = "授权应用不匹配（apiKey、apiSecret）"
        map[18301] = "SDK 未初始化"
        map[18302] = "SDK 初始化失败"
        map[18303] = "SDK 已经初始化"
        map[18304] = "不合法参数"
        map[18311] = "SDK 同一能力并发路数超出最大限制"
        map[18312] = "此实例已处在运行态，禁止单实例并发运行"
        map[18400] = "工作目录无写权限"
        map[18402] = "文件打开失败"
        map[18500] = "未找到该参数 Key"
        map[18501] = "参数范围溢出，不满足约束条件"
        map[18502] = "SDK 初始化参数为空"
        map[18503] = "SDK 初始化参数中 appId 为空"
        map[18504] = "SDK 初始化参数中 apiKey 为空"
        map[18505] = "SDK 初始化参数中 apiSecret 为空"
        map[18509] = "必填参数缺失"
        map[18700] = "通用网络错误"
        map[18701] = "网络不通"
        map[18702] = "网关检查不过"
        map[18703] = "云端响应格式不对"
        map[18705] = "应用 ApiKey & ApiSecret 校验失败"
        map[18707] = "授权已过期"
        map[18708] = "无可用授权"
        map[18712] = "网络请求 404 错误"
        map[18713] = "设备指纹安全等级不匹配"
        map[18714] = "应用信息有误"
        map[18717] = "SDK授权不足"
        map[18801] = "连接建立出错"
        map[18802] = "结果等待超时"
        map[18803] = "连接状态异常"
        map[18902] = "并发超过路数限制"
        map[18903] = "大模型规划步骤为空"
        map[18904] = "插件未找到"
        map[18906] = "与大模型交互次数超限制"
        map[18907] = "运行超限制时长"
        map[18908] = "大模型返回结果格式异常"
        map[18951] = "同一流式大模型会话，禁止并发交互请求"
        map[18952] = "输入文本格式或内容非法"
    }
}