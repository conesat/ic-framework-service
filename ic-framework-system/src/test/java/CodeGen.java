import cn.icframework.core.utils.MD5Util;
import cn.icframework.core.utils.RsaUtils;
import cn.icframework.system.module.setting.pojo.vo.ActivationInfoVO;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;

/**
 * 生成激活码
 *
 * @author iceFire
 * @since 2023/6/10
 */
public class CodeGen {
    private String rsaPrivateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIyqgeQbrUQq2kPcEzW2M4Z0PjQ0tNmQeBkccqptI2lt4pHjVRHIupsSofzyNLV76DbI102/YJmBBnVzMj1RjmEfEjbGQ2tRVvluX8CgYNJmlpPqubIdi+SQHLZmzLDaxyUZmGW21V2X6gkmXk/huwfDHy61O72gVdTNFCwgeU0dAgMBAAECgYAPjXKbE+xOUyaaxiRhlemQfh26i/FK9MwyipCrAzIdIAci9i2br8PiDWdXnjfxsOd11zLm57f4gOmD5t6xJOlG7PLdAsN+flWTubIQxHFY5SIs8X/pR323xEGzhCIrsGUjtvtjbfBgbFTwxKB1sb6fxwFKsMQ9geiCVs4AHpFSwQJBAOao8VVurAUCW8vBAGxkjtLtgDJ3AZjk5ifQPUSt4H4lMjnooAr4q5T45vlPb4Q0Oh4E+FCART8QE1aEqofF5QkCQQCcHpXdWLHzFbn+1qBRThx8KeML9kdlzzmxGNQ4LWBGjrm0r8HzPDblthgELGacizN6hwoEY+nIk4sUVOWPUqB1AkEAz7g21ZP8D+rG1iAyDGsmf+doOooXgyQn7JTIiC/56j//Ek1ey2KTym24O66Ao9MQv3YEVSKeuaL1d0Wz+N+LWQJAHpDCaJ634E7Npn4gzypK22hcAniKS/2BXxHsYr9HjSjeZOjQQS311Y2MMCz9PgLm5kNQC8IAAHwai7V/aEsWDQJAXtEJGbJhgeejZqf7Ex9tJ0kpRCifnC7lO0YFStFlqEFA12nUf3jZaSs0afGzFNos1kK/rjH5khszWslD2Aib1A==";


    @Test
    public void getCode() {
        ActivationInfoVO activationInfoVO = new ActivationInfoVO();
        activationInfoVO.setCode("0001");
        activationInfoVO.setAuthor("hg");
        activationInfoVO.setForever(true);
        activationInfoVO.setGenerationTime(System.currentTimeMillis());
        activationInfoVO.setOutTime(System.currentTimeMillis() + (60 * 24 * 60 * 60 * 1000l));

        activationInfoVO.setGrant("IceFire");
        activationInfoVO.setMd5Hash(MD5Util.encode(activationInfoVO.getMd5Code()));

        try {
            System.out.println(RsaUtils.privateEncrypt(JSONObject.toJSONString(activationInfoVO), rsaPrivateKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
