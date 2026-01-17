package hexa.chat.domain.photo.rule;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class Base64ImageValidator implements ConstraintValidator<ValidBase64Image, String> {
    private long maxSize;

    @Override
    public void initialize(ValidBase64Image constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        // 1. 필수값 체크
        if (value == null || value.isEmpty()) {
            addViolation(context, "이미지 데이터는 필수입니다.");
            return false;
        }

        // 2. 용량 체크 (maxSize는 1MB 등으로 설정된 값)
        if (value.length() > maxSize * 1.33) {
            addViolation(context, "파일 크기가 너무 큽니다. (최대 " + (maxSize / 1024) + "KB)");
            return false;
        }

        // 3. 포맷 체크
        if (!value.startsWith("data:image/")) {
            addViolation(context, "유효한 이미지 형식이 아닙니다. (data:image/... 시작 필수)");
            return false;
        }

        try {
            String[] parts = value.split(",");
            if (parts.length < 2) {
                addViolation(context, "Base64 데이터 형식이 잘못되었습니다.");
                return false;
            }

            String base64Data = parts[1];
            byte[] decodedBytes = Base64.getDecoder().decode(base64Data);

            // 4. 실제 이미지 파일인지 검증
            if (ImageIO.read(new ByteArrayInputStream(decodedBytes)) == null) {
                addViolation(context, "읽을 수 없는 이미지 파일입니다.");
                return false;
            }

            return true;
        } catch (Exception e) {
            addViolation(context, "이미지 처리 도중 오류가 발생했습니다.");
            return false;
        }
    }

    private void addViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
            .addConstraintViolation();
    }
}