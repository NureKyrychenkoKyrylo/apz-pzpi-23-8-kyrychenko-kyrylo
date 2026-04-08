/**
* Запити:
* - поясни, як патерн Facade допомагає зменшити зв’язність між клієнтським кодом та складною підсистемою на прикладі Java;
* - напиши приклад коду мовою Java, що ілюструє роботу патерна Facade для підсистеми відеоконвертації, приховуючи складні класи кодеків та мікшерів;
* - які основні відмінності між патернами Facade та Adapter? Наведи порівняльну характеристику.
* - як патерн Facade допомагає реалізувати принцип низької зв’язності?
*/



// --- КЛАСИ СКЛАДНОЇ ПІДСИСТЕМИ (Subsystem Classes) ---

class VideoFile {
    private String name;
    private String codecType;

    public VideoFile(String name) {
        this.name = name;
        this.codecType = name.substring(name.indexOf(".") + 1);
    }

    public String getCodecType() {
        return codecType;
    }

    public String getName() {
        return name;
    }
}

interface Codec {
    String getType();
}

class MPEG4CompressionCodec implements Codec {
    public String getType() { return "mp4"; }
}

class OggCompressionCodec implements Codec {
    public String getType() { return "ogg"; }
}

class CodecFactory {
    public static Codec extract(VideoFile file) {
        String type = file.getCodecType();
        if (type.equals("mp4")) {
            System.out.println("CodecFactory: extracting mpeg4 audio...");
            return new MPEG4CompressionCodec();
        } else {
            System.out.println("CodecFactory: extracting ogg audio...");
            return new OggCompressionCodec();
        }
    }
}

class BitrateReader {
    public static VideoFile read(VideoFile file, Codec codec) {
        System.out.println("BitrateReader: reading file...");
        return file;
    }

    public static VideoFile convert(VideoFile buffer, Codec codec) {
        System.out.println("BitrateReader: writing file...");
        return buffer;
    }
}

class AudioMixer {
    public String fix(VideoFile result) {
        System.out.println("AudioMixer: fixing audio...");
        return "tmp_audio_fixed";
    }
}

// --- КЛАС ФАСАДУ (Facade) ---

/**
 * Фасад надає єдиний метод для складної операції конвертації.
 * Він приховує від клієнта роботу з фабриками, кодеками та іншими налаштуваннями.
 */
class VideoConverter {
    public String convertVideo(String fileName, String format) {
        System.out.println("VideoConverter: conversion started.");
        
        VideoFile file = new VideoFile(fileName);
        Codec sourceCodec = CodecFactory.extract(file);
        
        Codec destinationCodec;
        if (format.equals("mp4")) {
            destinationCodec = new MPEG4CompressionCodec();
        } else {
            destinationCodec = new OggCompressionCodec();
        }
        
        VideoFile buffer = BitrateReader.read(file, sourceCodec);
        VideoFile intermediateResult = BitrateReader.convert(buffer, destinationCodec);
        String resultAudio = (new AudioMixer()).fix(intermediateResult);
        
        System.out.println("VideoConverter: conversion completed.");
        return "converted_video." + format;
    }
}

// --- КЛАС КЛІЄНТА (Client) ---

public class MainApp {
    public static void main(String[] args) {
        /**
         * Клієнтський код не знає про внутрішню будову сторонньої бібліотеки.
         * Весь інтерфейс взаємодії зводиться до роботи з одним класом-фасадом.
         */
        VideoConverter converter = new VideoConverter();
        
        // Виклик однієї команди замість десятків кроків ініціалізації підсистеми
        String resultFile = converter.convertVideo("funny-cats-video.ogg", "mp4");
        
        System.out.println("Результат операції: " + resultFile);
    }
}
